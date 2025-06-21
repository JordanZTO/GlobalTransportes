// Função para prévia de imagem
function previewImage(input, previewId) {
  const preview = document.getElementById(previewId);
  const label = input.previousElementSibling;
  
  if (input.files && input.files[0]) {
    const reader = new FileReader();
    
    reader.onload = function(e) {
      preview.innerHTML = `
        <div class="preview-container">
          <img src="${e.target.result}" alt="Preview" />
          <button type="button" class="remove-image" onclick="removeImage('${previewId}', '${input.id}')">×</button>
        </div>`;
      label.style.display = 'none';
    }
    
    reader.readAsDataURL(input.files[0]);
  } else {
    preview.innerHTML = '';
    label.style.display = 'flex';
  }
}

// Função para remover imagem
function removeImage(previewId, inputId) {
  const preview = document.getElementById(previewId);
  const input = document.getElementById(inputId);
  const label = input.previousElementSibling;
  
  preview.innerHTML = '';
  input.value = '';
  label.style.display = 'flex';
}

// Função para validar formulário de motorista
function validarFormularioMotorista() {
  // Campos obrigatórios
  const camposObrigatorios = [
    'nome_completo', 'cpf', 'logradouro', 'numero', 'bairro', 'cidade', 'estado', 'cep', 'pais',
    'telefone_pessoal', 'email_comercial', 'numero_cnh', 'numero_antt',
    'fotoCnh', 'fotoFrente', 'fotoPlaca',
    'numeroCrlv', 'placaVeiculo', 'ano', 'fabricante', 'modelo', 'cor', 'quantidadeEixo',
    'email', 'senha'
  ];

  // Verificar cada campo obrigatório
  for (const campo of camposObrigatorios) {
    const elemento = document.querySelector(`[name="${campo}"]`);
    if (!elemento || !elemento.value.trim()) {
      alert(`O campo ${campo.replace(/_/g, ' ')} é obrigatório!`);
      elemento.focus();
      return false;
    }
  }

  // Verificar campos numéricos
  const camposNumericos = ['ano', 'quantidadeEixo', 'peso', 'altura', 'comprimento', 'largura'];
  for (const campo of camposNumericos) {
    const elemento = document.querySelector(`[name="${campo}"]`);
    if (isNaN(elemento.value) || elemento.value.trim() === '') {
      alert(`O campo ${campo.replace(/_/g, ' ')} deve ser um número válido!`);
      elemento.focus();
      return false;
    }
  }

  // Verificar uploads de imagens
  const imagens = ['fotoCnh', 'fotoFrente', 'fotoPlaca'];
  for (const imagem of imagens) {
    const elemento = document.querySelector(`[name="${imagem}"]`);
    if (!elemento.files || elemento.files.length === 0) {
      alert(`É obrigatório fazer o upload da ${imagem.replace('foto', 'foto da ')}!`);
      elemento.focus();
      return false;
    }
  }

  // Verificar se possuiLona foi selecionado
  const possuiLona = document.querySelector('input[name="possuiLona"]:checked');
  if (!possuiLona) {
    alert('É obrigatório selecionar se o veículo possui lona!');
    return false;
  }

  return true;
}

document.addEventListener('DOMContentLoaded', function() {
  const cpfCnpjInput = document.getElementById('cpfCnpj');
  if (cpfCnpjInput) {
    cpfCnpjInput.addEventListener('input', function () {
      // Permite apenas números, ponto, barra e hífen
      this.value = this.value.replace(/[^0-9./-]/g, '');
    });
  }

  // Validação do formulário
  const form = document.querySelector('form');
  if (form) {
    form.addEventListener('submit', function(e) {
      // Verifica se é o formulário de motorista
      if (form.classList.contains('moto-cadastro-form')) {
        if (!validarFormularioMotorista()) {
          e.preventDefault();
          return;
        }
      }

      const passwordInput = form.querySelector('input[type="password"]');
      if (passwordInput) {
        const passwordValue = passwordInput.value;
        const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
        if (!passwordRegex.test(passwordValue)) {
          alert('A senha deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma letra minúscula e um número.');
          e.preventDefault();
        }
      }
    });
  }

  const dataVisivel = document.getElementById('dataNascimentoVisivel');
  const dataReal = document.getElementById('dataNascimentoReal');
  const calendarIcon = document.getElementById('calendarIcon');

  function abrirCalendario() {
    dataReal.showPicker();
  }

  if (dataVisivel && dataReal && calendarIcon) {
    dataVisivel.addEventListener('click', abrirCalendario);
    calendarIcon.addEventListener('click', abrirCalendario);

    dataReal.addEventListener('change', function() {
      if (this.value) {
        const [ano, mes, dia] = this.value.split('-');
        dataVisivel.value = `${dia}/${mes}/${ano}`;
      } else {
        dataVisivel.value = '';
      }
    });
  }
});
