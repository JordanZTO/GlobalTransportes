document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    if (!form) return;
    form.addEventListener('submit', function(e) {
        let erro = false;
        let mensagens = [];

        // CNH
        const removerCnh = form.querySelector('input[name="removerFotoCnh"]');
        const fileCnh = form.querySelector('input[name="fotoCnh"]');
        if (removerCnh && removerCnh.checked && (!fileCnh || !fileCnh.files || fileCnh.files.length === 0)) {
            erro = true;
            mensagens.push('Se remover a foto da CNH, é obrigatório inserir uma nova.');
        }

        // Frente
        const removerFrente = form.querySelector('input[name="removerFotoFrente"]');
        const fileFrente = form.querySelector('input[name="fotoFrente"]');
        if (removerFrente && removerFrente.checked && (!fileFrente || !fileFrente.files || fileFrente.files.length === 0)) {
            erro = true;
            mensagens.push('Se remover a foto da Frente do Caminhão, é obrigatório inserir uma nova.');
        }

        // Placa
        const removerPlaca = form.querySelector('input[name="removerFotoPlaca"]');
        const filePlaca = form.querySelector('input[name="fotoPlaca"]');
        if (removerPlaca && removerPlaca.checked && (!filePlaca || !filePlaca.files || filePlaca.files.length === 0)) {
            erro = true;
            mensagens.push('Se remover a foto da Placa do Caminhão, é obrigatório inserir uma nova.');
        }

        if (erro) {
            e.preventDefault();
            alert(mensagens.join('\n'));
            return;
        }

        // Feedback visual no botão de salvar
        var btn = document.getElementById('btnSalvar');
        if (btn) {
            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Salvando...';
        }
    });
});
