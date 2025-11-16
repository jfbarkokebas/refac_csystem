export function initClientSelect(selector, siteRoot) {
  const searchUrl = siteRoot + "/clients?action=select";

  $(selector).select2({
    placeholder: 'Selecione o cliente',
    width: '100%',
    minimumInputLength: 1,
    ajax: {
      url: searchUrl,
      dataType: 'json',
      delay: 250,
      data: params => ({ query: params.term }),
      processResults: data => ({
        results: data.map(c => ({
          id: c.id,
          text: c.name
        }))
      }),
      cache: true
    },
    language: {
      inputTooShort: () => 'Digite pelo menos 1 caractere',
      noResults: () => 'Nenhum cliente encontrado',
      searching: () => 'Buscando...'
    }
  });
}
