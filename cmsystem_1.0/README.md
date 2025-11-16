# Commodities System

## Descrição

Projeto desenvolvido com Java 11, JDBC, JSP, Javascript, HTML, CSS, Mysql e Wildfly.

## Tecnologias Utilizadas

Este projeto utiliza as seguintes tecnologias:

- **[Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)**: Linguagem de programação utilizada no backend.
- **[Wildfly 30.0](https://www.wildfly.org/downloads/)**: Servidor de aplicações usado para executar o projeto.
- **[MySQL 8.0.40](https://dev.mysql.com/downloads/mysql/)**: Banco de dados relacional utilizado para armazenar as informações da aplicação.

### Classes Service:

# SaleService

A classe `SaleService` é responsável por gerenciar as operações relacionadas às **vendas**, incluindo o cálculo de pendências, integração com lotes de compra/fornecimento, atualização de vendas e geração de informações financeiras.  

Ela atua como camada de **serviço**, utilizando o `SaleDAO` para persistência e recuperação dos dados no banco.  
Também contém regras de negócio importantes, como validação de pendências, controle de estoque via batches (FIFO) e cálculo do **Custo Médio Ponderado (CMP)**.  

---

## Métodos Públicos

- **saveSaleAndReturnList(Sale sale)**  
  Salva uma nova venda, atualiza lotes de estoque, calcula pendência/excesso e retorna a lista atualizada de vendas.  

- **calculatePendency(Sale sale)**  
  Calcula a pendência de uma venda com base apenas no estoque disponível.  

- **calculatePendencyWithBatches(Sale sale, List<? extends Batch> batchList, Double stockQuantity, boolean useQuantityInStock)**  
  Calcula a pendência e o excesso considerando tanto lotes fornecidos quanto a quantidade em estoque.  

- **getSaleList()**  
  Retorna a lista das últimas 500 vendas.  

- **deleteSaleAndReturnList(String stSaleId)**  
  Exclui uma venda (respeitando regras de negócio) e retorna a lista atualizada de vendas.  

- **getSaleById(String saleID)**  
  Busca uma venda específica pelo seu ID.  

- **updateSale(Sale sale, Boolean useQuantityInStock)**  
  Atualiza os dados de uma venda, recalculando pendências, lotes e excesso.  

- **getSalesByProduct(String productID)**  
  Lista todas as vendas de um produto específico.  

- **getSalesByCustomerName(String customerName)**  
  Lista vendas de um cliente específico.  

- **getSalesCompleted()**  
  Retorna vendas concluídas (últimas 500), incluindo cálculo de lucro.  

- **updateSalePendency(Sale sale)**  
  Recalcula a pendência de uma venda e atualiza no banco.  

- **getConcludedSalesByProduct(String productID)**  
  Lista vendas concluídas para um determinado produto.  

- **getConcludedSalesByCostumerName(String customerName)**  
  Lista vendas concluídas para um determinado cliente.  

- **getActualCMP(Long productID)**  
  Retorna o **Custo Médio Ponderado (CMP)** de um produto com base nos lotes disponíveis.  

- **getFinancesInfoFromOwnerStock()**  
  Retorna informações financeiras consolidadas do estoque (café e pimenta).  

- **canEditSale(Sale sale)**  
  Verifica se uma venda pode ser editada com base nas quantidades já consumidas.  

- **provisoryVerifyIfSaleCanBeEdited(Sale newSale)**  
  Verificação provisória para validar se uma venda pode ser editada (quantidade/preço).  

- **getBatchesBySaleId(String saleId)**  
  Busca os lotes vinculados a uma venda.  

- **setSaleAsPendingDelivery(String saleID)**  
  Marca uma venda como **pendente de entrega** e retorna a lista de vendas atualizada.  

- **getUndeliveredSales()**  
  Retorna todas as vendas não entregues.  

---

## Principais Regras de Negócio

- Utiliza **FIFO (First In, First Out)** para dar baixa nos lotes de estoque.  
- Controla **pendências** quando a quantidade vendida excede o estoque disponível.  
- Calcula **excesso** quando a quantidade fornecida supera a quantidade da venda.  
- Mantém registro de **CMP (Custo Médio Ponderado)** atualizado após cada operação.  
- Impede exclusão de vendas que já possuem lotes atrelados a clientes diferentes do padrão.  


### AVISO IMPORTANTE:
	Para o sistema funcionar adequadamente o proprietario deve estar setado como cliente e com ID = 1

