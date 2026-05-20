function initFileUpload(inputId, nameSpanId, buttonId, maxBytes, previewImgId) {
    const input = document.getElementById(inputId);
    const nameEl = document.getElementById(nameSpanId);
    const button = document.getElementById(buttonId);
    const previewImg = previewImgId ? document.getElementById(previewImgId) : null;

    if (!input || !nameEl || !button) {
        return;
    }

    button.addEventListener('click', function () {
        input.click();
    });

    input.addEventListener('change', function () {
        const file = input.files[0];
        if (!file) {
            nameEl.textContent = 'No file selected';
            nameEl.classList.remove('is-error');
            return;
        }
        if (file.size > maxBytes) {
            nameEl.textContent = 'File too large (max ' + Math.round(maxBytes / (1024 * 1024)) + ' MB)';
            nameEl.classList.add('is-error');
            input.value = '';
            return;
        }
        nameEl.classList.remove('is-error');
        nameEl.textContent = file.name;
        if (previewImg) {
            previewImg.src = URL.createObjectURL(file);
        }
    });
}
