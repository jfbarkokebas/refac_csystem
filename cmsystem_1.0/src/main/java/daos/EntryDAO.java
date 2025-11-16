package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connection.ConnectionFactory;
import dtos.TransactionsDTO;
import enums.TypeEntry;
import exceptions.AtomicStockQueryException;
import exceptions.AtomicTransactionException;
import exceptions.ResulSetDataNotFinded;
import model.Debit;
import model.Entry;
import model.EntryTransaction;
import model.Stock;
import utils.Const;
import utils.UtilsDao;

public class EntryDAO {

	final Logger logger = LoggerFactory.getLogger(EntryDAO.class);

	public Long saveFirstEntry(Entry entry, Stock stock, EntryTransaction transactionEntry,
			Boolean shouldADebitBeCreated) throws Exception {

		Long persistedStockEntryID = null;

		if (entry == null) {
			throw new IllegalArgumentException("A entrada no estoque não pode ser NULL");
		}

		Connection connection = null;
		PreparedStatement entryStatement = null;
		PreparedStatement stockStatement = null;
		PreparedStatement debitStatement = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String entrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" + " VALUES(?, ?, ?, ?, ?)";

			entryStatement = connection.prepareStatement(entrySql, Statement.RETURN_GENERATED_KEYS);

			entryStatement.setLong(1, entry.getClientId());
			entryStatement.setLong(2, entry.getProduct_id());
			entryStatement.setDouble(3, entry.getQuantity());
			entryStatement.setTimestamp(4, entry.getEntryDate());
			entryStatement.setString(5, entry.getObservation());

			int entryRowsAffected = entryStatement.executeUpdate();
			rs = entryStatement.getGeneratedKeys();

			if (rs.next()) {
				persistedStockEntryID = rs.getLong(1);
				transactionEntry.setEntryID(persistedStockEntryID);
			}

			if (entryRowsAffected < 0) {
				throw new AtomicTransactionException("Falha salvar a primeira entrada.");
			}

			insertEntryTransaction(connection, transactionEntry);

			String sql = "INSERT INTO cm_system.stock (client_id, product_id, quantity)"
					+ " VALUES (?, ?, ?)";

			stockStatement = connection.prepareStatement(sql);
			stockStatement.setLong(1, stock.getClientId());
			stockStatement.setLong(2, stock.getProductId());
			stockStatement.setDouble(3, stock.getQuantity());

			int stockRowsAffected = stockStatement.executeUpdate();
			if (stockRowsAffected < 1) {
				throw new AtomicTransactionException("Falha ao estocar pela primeira vez.");
			}

			if (shouldADebitBeCreated) {

				String debitSql = "INSERT INTO cm_system.debits (client_id, product_id, debit)  VALUES (?, ?, 0.00)";
				debitStatement = connection.prepareStatement(debitSql);
				debitStatement.setLong(1, stock.getClientId());
				debitStatement.setLong(2, stock.getProductId());

				int debitRowsAffected = debitStatement.executeUpdate();
				if (debitRowsAffected > 1) {
					throw new AtomicTransactionException("Falha criar um registro na tabela debits.");
				}
			}

			connection.commit();

		} catch (Exception e) {
			logger.error("saveFirstEntry() -> Erro: {}", e.getMessage());
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("Erro ao fazer rollback: {}", ex.getMessage());
				}
			}
			throw e;

		} finally {
			if (rs != null)
				rs.close();
			if (entryStatement != null)
				entryStatement.close();
			if (stockStatement != null)
				stockStatement.close();
			if (debitStatement != null)
				debitStatement.close();
			if (connection != null)
				connection.close();
		}

		return persistedStockEntryID;

	}

	public boolean saveEntry(Entry entry, EntryTransaction transactionEntry) throws SQLException {

		boolean result = false;
		Long persistedStockEntryID = null;

		if (entry == null) {
			throw new IllegalArgumentException("A entrada no estoque não pode ser NULL");
		}

		Connection connection = null;
		PreparedStatement entryStatement = null;
		PreparedStatement transactionStmt = null;
		PreparedStatement stockStatement = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String entrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" + " VALUES( ?, ?, ?, ?, ?)";

			entryStatement = connection.prepareStatement(entrySql, Statement.RETURN_GENERATED_KEYS);
			entryStatement.setLong(1, entry.getClientId());
			entryStatement.setLong(2, entry.getProduct_id());
			entryStatement.setDouble(3, entry.getQuantity());
			entryStatement.setTimestamp(4, entry.getEntryDate());
			entryStatement.setString(5, entry.getObservation());

			int entryRowsAffected = entryStatement.executeUpdate();
			rs = entryStatement.getGeneratedKeys();

			if (rs.next()) {
				persistedStockEntryID = rs.getLong(1);
				transactionEntry.setEntryID(persistedStockEntryID);
			}

			if (entryRowsAffected < 0) {
				throw new AtomicTransactionException("Falha salvar a primeira entrada.");
			}

			String transactioSql = "INSERT INTO cm_system.entry_transactions (entry_id, type_entry, client_entry, owner_entry)"
					+ " VALUES (?, ?, ?, ?)";

			transactionStmt = connection.prepareStatement(transactioSql);

			transactionStmt.setLong(1, transactionEntry.getEntryID());
			transactionStmt.setString(2, transactionEntry.getTypeEntry().name());
			transactionStmt.setDouble(3, transactionEntry.getClientEntry());
			transactionStmt.setDouble(4, transactionEntry.getOwnerEntry());

			int transactionRowsAffected = transactionStmt.executeUpdate();

			if (transactionRowsAffected < 0) {
				throw new AtomicTransactionException("Falha salvar transactionEntry na primeira entrada.");
			}

			String sql = "UPDATE cm_system.stock SET quantity = quantity + ?"
					+ " WHERE client_id = ? AND product_id = ?";

			stockStatement = connection.prepareStatement(sql);
			stockStatement.setDouble(1, entry.getQuantity());
			stockStatement.setLong(2, entry.getClientId());
			stockStatement.setLong(3, entry.getProduct_id());

			int stockRowsAffected = stockStatement.executeUpdate();
			if (stockRowsAffected < 1) {
				throw new AtomicTransactionException("Falha ao atualizar o estoque.");
			}

			connection.commit();
			result = true;

		} catch (Exception e) {
			logger.error("saveEntry() -> Erro: {}", e.getMessage());
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("Erro ao fazer rollback: {}", ex.getMessage());
				}
			}
			throw e;

		} finally {
			if (rs != null)
				rs.close();
			if (entryStatement != null)
				entryStatement.close();
			if (stockStatement != null)
				stockStatement.close();
			if (connection != null)
				connection.close();
		}

		return result;
	}

	public Map<String, Object> getGeneralStockInfo() throws Exception {
		Map<String, Object> result = new HashMap<>();

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		for (int i = 1; i < 3; i++) {
			String key = "";

			try {
				String sql = "SELECT SUM(quantity) FROM cm_system.entries WHERE product_id = " + i;
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {
					key = "cafe";
					if (i > 1)
						key = "pimenta";
					result.put(key, rs.getDouble(1));
				}

			} catch (Exception e) {
				logger.info("getGeneralStockInfo() -> Erro ao buscar o estoque de {}. {}", key, e.getMessage());
				throw e;

			} finally {
				UtilsDao.closeDAOResources(rs, stmt, connection, logger);
			}
		}

		if (result.size() > 1) {
			return result;
		} else {
			throw new AtomicStockQueryException("A atomicidade não foi bem sucedida no banco de dados");
		}
	}

	public HashMap<String, Object> getTableData() throws Exception {

		HashMap<String, Object> result = new HashMap<>();
		List<TransactionsDTO> dtoList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT  c.name, p.name AS product, se.quantity, se.entry_date, se.observation, se.id,  p.unit AS product_unit"
					+ " FROM cm_system.entries AS se" + " INNER JOIN  clients c ON se.client_id = c.id"
					+ " INNER JOIN products p ON se.product_id = p.id ORDER BY entry_date  DESC;";

			connection = ConnectionFactory.conectar();
		
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				TransactionsDTO dto = new TransactionsDTO();
				String name = rs.getString("name");
				dto.setClientName(name);
				dto.setId(rs.getLong("id"));
				dto.setProductName(rs.getString("product"));
				dto.setFormatedQuantity(rs.getDouble("quantity"));
				dto.setUnit(rs.getString("product_unit"));
				dto.setEntryDate(rs.getTimestamp("entry_date"));
				dto.setObservation(rs.getString("observation"));

				dtoList.add(dto);

			}

			result.put("tableData", dtoList);
			result.put("count", dtoList.size());

		} catch (Exception e) {
			logger.error("getTableData() -> Erro ao buscar o estoque de {}", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public HashMap<String, Object> getTableDataByProduct(String product) throws Exception {

		if (product == null) {
			throw new IllegalArgumentException("A busca no estoque não pode ser NULL");
		}

		HashMap<String, Object> result = new HashMap<>();
		List<TransactionsDTO> dtoList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT c.name, p.name AS product, se.quantity, se.entry_date, se.observation, se.id,  p.unit AS product_unit "
					+ "FROM cm_system.entries AS se " + "INNER JOIN clients c ON se.client_id = c.id "
					+ "INNER JOIN products p ON se.product_id = p.id " + "WHERE p.name = ? ORDER BY entry_date  DESC;";

			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, product);
			rs = stmt.executeQuery();

			while (rs.next()) {
				TransactionsDTO dto = new TransactionsDTO();
				String name = rs.getString("name");
				dto.setClientName(name);
				dto.setId(rs.getLong("id"));
				dto.setProductName(rs.getString("product"));
				dto.setFormatedQuantity(rs.getDouble("quantity"));
				dto.setUnit(rs.getString("product_unit"));
				dto.setEntryDate(rs.getTimestamp("entry_date"));
				dto.setObservation(rs.getString("observation"));

				dtoList.add(dto);

			}

			result.put("tableData", dtoList);
			result.put("count", dtoList.size());

		} catch (Exception e) {
			logger.error("getTableDataByProduct() -> Erro ao buscar o estoque do produto: {}. {}", product, e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public HashMap<String, Object> getTableDataByClient(String searchName) throws Exception {

		HashMap<String, Object> result = new HashMap<>();
		List<TransactionsDTO> dtoList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT  c.name, p.name AS product, se.quantity, se.entry_date, se.observation, se.id, p.unit AS product_unit"
					+ " FROM cm_system.entries AS se" + " INNER JOIN  clients c ON se.client_id = c.id"
					+ " INNER JOIN products p ON se.product_id = p.id"
					+ " WHERE UPPER(c.name) like UPPER(?) ORDER BY entry_date  DESC;";

			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + searchName + "%");
			rs = stmt.executeQuery();

			while (rs.next()) {
				TransactionsDTO dto = new TransactionsDTO();
				String name = rs.getString("name");
				dto.setClientName(name);
				dto.setId(rs.getLong("id"));
				dto.setProductName(rs.getString("product"));
				dto.setFormatedQuantity(rs.getDouble("quantity"));
				dto.setUnit(rs.getString("product_unit"));
				dto.setEntryDate(rs.getTimestamp("entry_date"));
				dto.setObservation(rs.getString("observation"));

				dtoList.add(dto);

			}

			result.put("tableData", dtoList);
			result.put("count", dtoList.size());

		} catch (Exception e) {
			logger.error("getTableDataByClient() -> Erro ao buscar o estoque por cliente. {}", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public Entry getStockEntryById(Long id) throws Exception {

		Entry result = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT id, client_id, product_id, quantity FROM cm_system.entries  WHERE id = ?";

			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new ResulSetDataNotFinded("Não foi encontrada uma entrada na base de dados com esse id.");
			}
			result = new Entry();
			result.setId(rs.getLong("id"));
			result.setClientId(rs.getLong("client_id"));
			result.setProductId(rs.getLong("product_id"));
			result.setQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getEntryById() -> Erro a entrada por id. {}", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public Map<String, Object> getEntryById(Long id) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT  c.name AS name, c.cpf AS cpf, p.name AS product,"
					+ " se.quantity, se.id, se.entry_date, se.observation, p.unit AS product_unit"
					+ " FROM cm_system.entries AS se" + " INNER JOIN  clients c ON se.client_id = c.id"
					+ " INNER JOIN products p ON se.product_id = p.id" + " WHERE se.id = ? ORDER BY entry_date DESC";

			connection = ConnectionFactory.conectar();
			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new ResulSetDataNotFinded("Não foi enconrado uma entrada na base de dados com esse id.");
			}

			result.put("entryID", rs.getLong("id"));
			result.put("clientName", rs.getString("name"));
			result.put("cpf", rs.getString("cpf"));
			result.put("product", rs.getString("product"));
			result.put("quantity", rs.getString("quantity"));
			result.put("productUnit", rs.getString("product_unit"));
			result.put("entryDate", rs.getTimestamp("entry_date"));

		} catch (Exception e) {
			logger.error("getEntryById() -> Erro ao buscar o estoque por cliente. {}", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public boolean deleteEntry(long entryId, Stock newStock) throws Exception {
		boolean result = false;

		Connection connection = null;
		PreparedStatement deleteEntryStmt = null;
		PreparedStatement updateStockStmt = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String sqlDelete = "DELETE FROM cm_system.entries WHERE id = ?";
			deleteEntryStmt = connection.prepareStatement(sqlDelete);
			deleteEntryStmt.setLong(1, entryId);
			deleteEntryStmt.executeUpdate();

			// remover a entrada do estoque
			String sqlUpdate = "UPDATE cm_system.stock SET quantity= ? WHERE id = ?";
			updateStockStmt = connection.prepareStatement(sqlUpdate);
			updateStockStmt.setDouble(1, newStock.getQuantity());
			updateStockStmt.setLong(2, newStock.getId());
			updateStockStmt.executeUpdate();

			connection.commit();
			result = true;

		} catch (Exception e) {
			logger.error("deleteEntry() -> Erro ao deletar a entrada de estoque: {}", e.getMessage());
			connection.rollback();
			throw e;

		} finally {

			if (deleteEntryStmt != null) {
				deleteEntryStmt.close();
			}
			if (updateStockStmt != null) {
				updateStockStmt.close();
			}
		}

		return result;
	}

	public Boolean handleQuitClientDebits(Entry entry, EntryTransaction transactionEntry, Double entryQuantity,
			Double debit, Double quitedQuantity) throws SQLException {

		Boolean result = false;
		Long persistedStockEntryID = null;

		Connection connection = null;
		PreparedStatement increasyOwnerStmt = null;
		PreparedStatement increasySuplierStmt = null;
		PreparedStatement decreasyStmt = null;
		PreparedStatement quitDebitStmt = null;
		PreparedStatement saveEntryStmt = null;
		PreparedStatement purchaseStmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String saveEntrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" + " VALUES(?, ?, ?, ?, ?)";
			saveEntryStmt = connection.prepareStatement(saveEntrySql, Statement.RETURN_GENERATED_KEYS);
			saveEntryStmt.setLong(1, entry.getClientId());
			saveEntryStmt.setLong(2, entry.getProduct_id());
			saveEntryStmt.setDouble(3, entry.getQuantity());
			saveEntryStmt.setTimestamp(4, entry.getEntryDate());
			saveEntryStmt.setString(5, entry.getObservation());
			int saveRow = saveEntryStmt.executeUpdate();
			rs = saveEntryStmt.getGeneratedKeys();

			if (saveRow < 1) {
				throw new AtomicTransactionException("A entrada não foi salva.");
			}

			if (rs.next()) {
				persistedStockEntryID = rs.getLong(1);
				transactionEntry.setEntryID(persistedStockEntryID);
			}

			insertEntryTransaction(connection, transactionEntry);

			String increasySuplierSQL = "UPDATE cm_system.stock SET quantity = (quantity + ?) WHERE product_id = ? AND client_id = ?";
			increasySuplierStmt = connection.prepareStatement(increasySuplierSQL);
			increasySuplierStmt.setDouble(1, entryQuantity);
			increasySuplierStmt.setLong(2, entry.getProduct_id());
			increasySuplierStmt.setLong(3, entry.getClientId());
			int increasyStockSuplier = increasySuplierStmt.executeUpdate();
			if (increasyStockSuplier < 1) {
				throw new AtomicTransactionException("Falha ao adicionar no estoque fornecedor.");
			}

			if (debit > 0.0) {

				String increasyOwnerSQL = "UPDATE cm_system.stock SET quantity = ? WHERE product_id = ? AND client_id = 1";
				increasyOwnerStmt = connection.prepareStatement(increasyOwnerSQL);
				increasyOwnerStmt.setDouble(1, quitedQuantity);
				increasyOwnerStmt.setLong(2, entry.getProduct_id());
				int increasyOwner = increasyOwnerStmt.executeUpdate();
				if (increasyOwner < 1) {
					throw new AtomicTransactionException("Falha ao adicionar no estoque proprietário.");
				}

				String decreasySQL = "UPDATE cm_system.stock SET quantity = (quantity - ?) WHERE product_id = ? AND client_id = ?";
				decreasyStmt = connection.prepareStatement(decreasySQL);
				decreasyStmt.setDouble(1, debit);
				decreasyStmt.setLong(2, entry.getProduct_id());
				decreasyStmt.setLong(3, entry.getClientId());
				int drecreasyDebit = decreasyStmt.executeUpdate();
				if (drecreasyDebit < 1) {
					throw new AtomicTransactionException("Falha ao descontar o débito.");
				}

				String quitDebitSql = "UPDATE cm_system.debits SET debit = 0.00 WHERE product_id = ? AND client_id = ?";
				quitDebitStmt = connection.prepareStatement(quitDebitSql);
				quitDebitStmt.setLong(1, entry.getProduct_id());
				quitDebitStmt.setLong(2, entry.getClientId());
				int quitDebit = quitDebitStmt.executeUpdate();
				if (quitDebit < 1) {
					throw new AtomicTransactionException("Falha ao zerar o débito.");
				}

				String paidOffSql = "UPDATE cm_system.purchases SET is_paid_off = true"
						+ " WHERE product_id = ? AND client_id = ?";
				purchaseStmt = connection.prepareStatement(paidOffSql);
				purchaseStmt.setLong(1, entry.getProduct_id());
				purchaseStmt.setLong(2, entry.getClientId());
				int purchasesRowsAffected = purchaseStmt.executeUpdate();
				if (purchasesRowsAffected < 1) {
					throw new AtomicTransactionException("Falha ao setar is_paid_off como true.");
				}
				
			}

			connection.commit();
			result = true;

		} catch (Exception e) {
			logger.error("handleClientDebits() -> Erro: {}", e.getMessage());
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("Erro ao fazer rollback: {}", ex.getMessage());
				}
			}
			throw e;

		} finally {
			
			UtilsDao.closeDAOResources(null, connection, logger, increasyOwnerStmt, increasySuplierStmt,
					decreasyStmt, quitDebitStmt, purchaseStmt);
		}

		return result;
	}

	public Boolean handleClientDebits(Entry entry, EntryTransaction transactionEntry, Double purchasedQuantity,
			Double debit, Double debitRemainder) throws SQLException {

		Boolean result = false;
		Long persistedStockEntryID = null;

		Connection connection = null;
		PreparedStatement increasyOwnerStmt = null;
		PreparedStatement updateClientStockStmt = null;
		PreparedStatement updateDebitStmt = null;
		PreparedStatement saveEntryStmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String saveEntrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" + " VALUES(?, ?, ?, ?, ?)";
			saveEntryStmt = connection.prepareStatement(saveEntrySql, Statement.RETURN_GENERATED_KEYS);
			saveEntryStmt.setLong(1, entry.getClientId());
			saveEntryStmt.setLong(2, entry.getProduct_id());
			saveEntryStmt.setDouble(3, entry.getQuantity());
			saveEntryStmt.setTimestamp(4, entry.getEntryDate());
			saveEntryStmt.setString(5, entry.getObservation());
			int saveRow = saveEntryStmt.executeUpdate();

			rs = saveEntryStmt.getGeneratedKeys();

			if (rs.next()) {
				persistedStockEntryID = rs.getLong(1);
				transactionEntry.setEntryID(persistedStockEntryID);
			}

			if (saveRow < 1) {
				throw new AtomicTransactionException("A entrada não foi salva.");
			}

			insertEntryTransaction(connection, transactionEntry);

			String updateClientStockSQL = "UPDATE cm_system.stock SET quantity = 0.0 WHERE product_id = ? AND client_id = ?";
			updateClientStockStmt = connection.prepareStatement(updateClientStockSQL);
			updateClientStockStmt.setLong(1, entry.getProduct_id());
			updateClientStockStmt.setLong(2, entry.getClientId());
			int increasyStockClient = updateClientStockStmt.executeUpdate();

			if (increasyStockClient < 1) {
				throw new AtomicTransactionException("Falha ao adicionar no estoque fornecedor.");
			}

			String increasyOwnerSQL = "UPDATE cm_system.stock SET quantity = (quantity + ?) WHERE product_id = ? AND client_id = 1";
			increasyOwnerStmt = connection.prepareStatement(increasyOwnerSQL);
			increasyOwnerStmt.setDouble(1, purchasedQuantity);
			increasyOwnerStmt.setLong(2, entry.getProduct_id());
			int increasyOwner = increasyOwnerStmt.executeUpdate();

			if (increasyOwner < 1) {
				throw new AtomicTransactionException("Falha ao adicionar no estoque proprietário.");
			}

			String updateDebitSql = "UPDATE cm_system.debits SET debit = ? WHERE product_id = ? AND client_id = ?";
			updateDebitStmt = connection.prepareStatement(updateDebitSql);
			updateDebitStmt.setDouble(1, debitRemainder);
			updateDebitStmt.setLong(2, entry.getProduct_id());
			updateDebitStmt.setLong(3, entry.getClientId());
			int quitDebit = updateDebitStmt.executeUpdate();
			if (quitDebit < 1) {
				throw new AtomicTransactionException("Falha ao atualizar o débito do cliente.");
			}

			connection.commit();
			result = true;

		} catch (Exception e) {
			logger.error("handleClientDebits() -> Erro: {}", e.getMessage());
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("Erro ao fazer rollback: {}", ex.getMessage());
				}
			}
			throw e;

		} finally {
			if (rs != null)
				rs.close();
			if (increasyOwnerStmt != null)
				increasyOwnerStmt.close();
			if (updateClientStockStmt != null)
				updateClientStockStmt.close();
			if (updateDebitStmt != null)
				updateDebitStmt.close();
			if (connection != null)
				connection.close();
		}

		return result;
	}
	
	public Boolean handleClientDebitsWithBatch(Entry entry,  EntryTransaction transactionEntry, Double purchasedQuantity,
			Double debit, Double debitRemainder) throws SQLException {

		Boolean result = false;
		Long persistedStockEntryID = null;

		Connection connection = null;
		PreparedStatement increasyOwnerStmt = null;
		PreparedStatement updateClientStockStmt = null;
		PreparedStatement updateDebitStmt = null;
		PreparedStatement saveEntryStmt = null;
		PreparedStatement insertBatchStmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			String saveEntrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" 
					+ " VALUES(?, ?, ?, ?, ?)";
			saveEntryStmt = connection.prepareStatement(saveEntrySql, Statement.RETURN_GENERATED_KEYS);
			saveEntryStmt.setLong(1, entry.getClientId());
			saveEntryStmt.setLong(2, entry.getProduct_id());
			saveEntryStmt.setDouble(3, entry.getQuantity());
			saveEntryStmt.setTimestamp(4, entry.getEntryDate());
			saveEntryStmt.setString(5, entry.getObservation());
			int saveRow = saveEntryStmt.executeUpdate();

			rs = saveEntryStmt.getGeneratedKeys();

			if (rs.next()) {
				persistedStockEntryID = rs.getLong(1);
				transactionEntry.setEntryID(persistedStockEntryID);
			}

			if (saveRow < 1) {
				throw new AtomicTransactionException("A entrada não foi salva.");
			}

			insertEntryTransaction(connection, transactionEntry);

			//abater o debito
			String updateClientStockSQL = "UPDATE cm_system.stock SET quantity = 0.0 WHERE product_id = ? AND client_id = ?";
			updateClientStockStmt = connection.prepareStatement(updateClientStockSQL);
			updateClientStockStmt.setLong(1, entry.getProduct_id());
			updateClientStockStmt.setLong(2, entry.getClientId());
			int increasyStockClient = updateClientStockStmt.executeUpdate();

			if (increasyStockClient < 1) {
				throw new AtomicTransactionException("Falha ao adicionar no estoque fornecedor.");
			}

			String increasyOwnerSQL = "UPDATE cm_system.stock SET quantity = (quantity + ?) WHERE product_id = ? AND client_id = ?";
			increasyOwnerStmt = connection.prepareStatement(increasyOwnerSQL);
			increasyOwnerStmt.setDouble(1, purchasedQuantity);
			increasyOwnerStmt.setLong(2, entry.getProduct_id());
			increasyOwnerStmt.setLong(3, Const.OWNER_ID);
			int increasyOwner = increasyOwnerStmt.executeUpdate();

			if (increasyOwner < 1) {
				throw new AtomicTransactionException("Falha ao adicionar no estoque proprietário.");
			}

			String updateDebitSql = "UPDATE cm_system.debits SET debit = ? WHERE product_id = ? AND client_id = ?";
			updateDebitStmt = connection.prepareStatement(updateDebitSql);
			updateDebitStmt.setDouble(1, debitRemainder);
			updateDebitStmt.setLong(2, entry.getProduct_id());
			updateDebitStmt.setLong(3, entry.getClientId());
			int quitDebit = updateDebitStmt.executeUpdate();
			if (quitDebit < 1) {
				throw new AtomicTransactionException("Falha ao atualizar o débito do cliente.");
			}
			
			connection.commit();
			result = true;

		} catch (Exception e) {
			logger.error("handleClientDebitsWithBatch() -> Erro: {}", e.getMessage());
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("Erro ao fazer rollback: {}", ex.getMessage());
				}
			}
			throw e;

		} finally {
			
			UtilsDao.closeDAOResources(rs, connection, logger, increasyOwnerStmt, updateClientStockStmt,
					updateDebitStmt, insertBatchStmt);
		}

		return result;
	}

	public Debit getClientDebit(Long productID, Long clientID) throws SQLException {
		Debit debit = null;

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();

			String debitSql = "SELECT id, debit, purchase_list, is_supplier, last_update FROM cm_system.debits WHERE product_id = ? AND client_id = ?";

			stmt = connection.prepareStatement(debitSql);
			stmt.setLong(1, productID);
			stmt.setLong(2, clientID);
			rs = stmt.executeQuery();

			if (rs.next()) {
				debit = new Debit();
				debit.setID(rs.getLong("id"));
				debit.setDebit(rs.getDouble("debit"));
				debit.setPurchaseList(rs.getString("purchase_list"));
				debit.setSupplier(rs.getBoolean("is_supplier"));
				debit.setLastUpdate(rs.getTimestamp("last_update"));

			}

		} catch (SQLException e) {
			logger.error("getClientDebit() -> Erro ao buscar o débito do cliente. {}", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return debit;
	}

	public Double getOwnerStockQuantity(Long productID) throws SQLException {
		Double result = null;

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();

			String debitSql = "SELECT quantity FROM cm_system.stock WHERE product_id = ? AND client_id = 1;";

			stmt = connection.prepareStatement(debitSql);
			stmt.setLong(1, productID);
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = rs.getDouble("quantity");
			}

		} catch (SQLException e) {
			logger.error("getOwnerStockQuantity() -> Erro ao buscar o aquantidade do estoque proprietário. {}", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return result;
	}

	public Boolean setPaidOffTRUE(Long id) throws SQLException {

		Boolean result;

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "UPDATE cm_system.purchases SET is_paid_off = TRUE" + " WHERE id = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, id);
			int rowsAffected = stmt.executeUpdate();
			result = rowsAffected > 0;

		} catch (SQLException e) {
			logger.error("setPaidOffTRUE() -> Erro ao setar como true a coluna is_paid_off. {}", e.getMessage());
			throw e;
		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return result;

	}

	public boolean deleteDebitList(Debit debitInDB) throws SQLException {
		Boolean result;

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "UPDATE cm_system.debits SET purchase_list = null WHERE id = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, debitInDB.getID());
			int rowsAffected = stmt.executeUpdate();
			result = rowsAffected > 0;

		} catch (SQLException e) {
			logger.error("deleteDebitList() -> Erro ao apagar a purchase_list.", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return result;

	}

	public EntryTransaction geTransactionEntry(Long stockEntryId) throws SQLException {

		EntryTransaction transaction = null;

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "SELECT id, type_entry, client_entry, owner_entry, created_at"
					+ " FROM cm_system.entry_transactions WHERE entry_id = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, stockEntryId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				transaction = new EntryTransaction();
				transaction.setId(rs.getLong("id"));
				transaction.setClientEntry(rs.getDouble("client_entry"));
				transaction.setOwnerEntry(rs.getDouble("owner_entry"));
				transaction.setCreatedAt(rs.getTimestamp("created_at"));
				String typeStr = rs.getString("type_entry");
				TypeEntry typeEntry = TypeEntry.fromParameter(typeStr);
				transaction.setTypeEntry(typeEntry);
				
			}

		} catch (SQLException e) {
			logger.error("getStockEntryId() -> Erro pegar a transactionEntry.", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return transaction;
	}

	public static void insertEntryTransaction(Connection connection, EntryTransaction transactionEntry)
			throws SQLException {
		if (connection == null || transactionEntry == null) {
			throw new IllegalArgumentException("insertTransactionEntry(): conexão ou objeto não pode ser null");
		}

		PreparedStatement stmt = null;
		try {
			String sql = "INSERT INTO cm_system.entry_transactions (entry_id, type_entry, client_entry, owner_entry)"
					+ " VALUES (?, ?, ?, ?)";

			stmt = connection.prepareStatement(sql);

			stmt.setLong(1, transactionEntry.getEntryID());
			stmt.setString(2, transactionEntry.getTypeEntry().name());
			stmt.setDouble(3, transactionEntry.getClientEntry());
			stmt.setDouble(4, transactionEntry.getOwnerEntry());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected < 1) {
				throw new AtomicTransactionException("Falha ao salvar transactionEntry.");
			}
		} finally {
			if (stmt != null)
				stmt.close(); 
		}
	}

	public boolean deleteStockEntry(Entry entryToDelete, EntryTransaction transaction) 
			throws SQLException {
		
		boolean result = false;

		Connection connection = null;
		PreparedStatement deleteStmt = null;
		PreparedStatement clientStmt = null;
		PreparedStatement ownerStmt = null;
		PreparedStatement debitStmt = null;

		try {
			connection = ConnectionFactory.conectar();
			connection.setAutoCommit(false);

			// deletar a entrada
			String sqlDelete = "DELETE FROM cm_system.entries WHERE id = ?";
			deleteStmt = connection.prepareStatement(sqlDelete);
			deleteStmt.setLong(1, entryToDelete.getId());
			deleteStmt.executeUpdate();

			// se transaction.getEntry > 0; abate do estoque do cliente
			if (transaction.getClientEntry() > 0) {
				String sqlUpdate = "UPDATE cm_system.stock" + " SET quantity= quantity - ?"
						+ " WHERE client_id = ? AND product_id = ?";

				clientStmt = connection.prepareStatement(sqlUpdate);
				clientStmt.setDouble(1, transaction.getClientEntry());
				clientStmt.setLong(2, entryToDelete.getClientId());
				clientStmt.setLong(3, entryToDelete.getProduct_id());
				clientStmt.executeUpdate();

			}

			// se transaction.ownerEntry > 0; abate do estoque do Bettim
			if (transaction.getOwnerEntry() > 0) {
				String sqlUpdate = "UPDATE cm_system.stock" + " SET quantity= quantity - ?"
						+ " WHERE client_id = 1 AND product_id = ?";

				clientStmt = connection.prepareStatement(sqlUpdate);
				clientStmt.setDouble(1, transaction.getOwnerEntry());
				clientStmt.setLong(2, entryToDelete.getProduct_id());
				clientStmt.executeUpdate();
			}
			
			//SE A ENTRADA ABATEU UM DÉBITO PRECISA INCREMENTAR O DEBITO  
			if ("REDUCE_DEBIT".equals(transaction.getTypeEntry().getDescription())) {
				
				String debitSql = "UPDATE cm_system.debits SET debit = debit + ?"
						+ " WHERE client_id = ? AND product_id = ?";
				
				debitStmt= connection.prepareStatement(debitSql);
				debitStmt.setDouble(1, transaction.getOwnerEntry());	
				debitStmt.setLong(2, entryToDelete.getClientId());
				debitStmt.setLong(3, entryToDelete.getProduct_id());
				debitStmt.executeUpdate();
			}
			
			connection.commit();
			result = true;

		} catch (SQLException e) {
			logger.error("deleteStockEntry() -> Erro ao deletar a entrada", e);

			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					logger.error("purchase() -> Erro ao fazer rollback da transação", ex);
				}
			}

			throw e;

		} finally {
			UtilsDao.closeDAOResources(null, deleteStmt, null, logger);
			UtilsDao.closeDAOResources(null, clientStmt, null, logger);
			UtilsDao.closeDAOResources(null, ownerStmt, null, logger);
			UtilsDao.closeDAOResources(null, debitStmt, connection, logger);
		}

		return result;
	}

	public Boolean increasePurchaseBatchQuantity(Long purchaseID, Double quantity) throws SQLException {
		
		Boolean result = false;

		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "UPDATE cm_system.purchase_batches SET quantity = quantity + ?"
					+ " WHERE purchase_id = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setDouble(1, quantity);
			stmt.setLong(2, purchaseID);
			int rowsAffected = stmt.executeUpdate();
			result = rowsAffected > 0;

		} catch (SQLException e) {
			logger.error("increasePurchaseBatchQuantity() -> Erro ao incrementar o a quantidade na batch", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return result;
		
	}
	
	private Long insertEntry(Connection connection, Entry entry) {
		
		Long entryId = null;
		
		PreparedStatement entryStatement = null;
		ResultSet rs = null;

		try {

			String entrySql = "INSERT INTO cm_system.entries"
					+ " (client_id, product_id, quantity, entry_date, observation)" + " VALUES(?, ?, ?, ?, ?)";

			entryStatement = connection.prepareStatement(entrySql, Statement.RETURN_GENERATED_KEYS);

			entryStatement.setLong(1, entry.getClientId());
			entryStatement.setLong(2, entry.getProduct_id());
			entryStatement.setDouble(3, entry.getQuantity());
			entryStatement.setTimestamp(4, entry.getEntryDate());
			entryStatement.setString(5, entry.getObservation());

			int entryRowsAffected = entryStatement.executeUpdate();
			rs = entryStatement.getGeneratedKeys();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return entryId;
	}

}
