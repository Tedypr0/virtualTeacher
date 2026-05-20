/**
 * Profile image cropper; cropped image is attached to the profile form on Save.
 */
function initProfileImageCropper(options) {
    const input = document.getElementById(options.inputId);
    const chooseBtn = document.getElementById(options.chooseBtnId);
    const form = document.getElementById(options.formId);
    const fileNameEl = document.getElementById(options.fileNameId);
    const previewImg = document.getElementById(options.previewId);
    const modalEl = document.getElementById(options.modalId);
    const cropImg = document.getElementById(options.cropImageId);
    const applyBtn = document.getElementById(options.applyBtnId);
    const cancelBtn = document.getElementById(options.cancelBtnId);
    const saveBtn = options.saveBtnId ? document.getElementById(options.saveBtnId) : null;
    const maxBytes = options.maxBytes || 20 * 1024 * 1024;
    const outputSize = options.outputSize || 512;

    if (!input || !chooseBtn || !form || !modalEl || !cropImg) {
        return;
    }

    let cropper = null;
    let croppedBlob = null;
    let cropSourceUrl = null;
    let selectedFileName = '';
    let dynamicImageInput = null;

    function revokeCropSource() {
        if (cropSourceUrl) {
            URL.revokeObjectURL(cropSourceUrl);
            cropSourceUrl = null;
        }
    }

    function destroyCropper() {
        if (cropper) {
            cropper.destroy();
            cropper = null;
        }
        cropImg.removeAttribute('src');
        revokeCropSource();
    }

    function removeDynamicImageInput() {
        if (dynamicImageInput && dynamicImageInput.parentNode) {
            dynamicImageInput.parentNode.removeChild(dynamicImageInput);
        }
        dynamicImageInput = null;
        form.removeAttribute('enctype');
    }

    function closeModal() {
        destroyCropper();
        if (window.jQuery && typeof jQuery(modalEl).modal === 'function') {
            jQuery(modalEl).modal('hide');
        } else {
            modalEl.style.display = 'none';
            modalEl.classList.remove('show');
        }
    }

    function openModal() {
        if (window.jQuery && typeof jQuery(modalEl).modal === 'function') {
            jQuery(modalEl).modal('show');
        } else {
            modalEl.style.display = 'block';
            modalEl.classList.add('show');
        }
    }

    function setFileName(text, isError) {
        if (!fileNameEl) {
            return;
        }
        fileNameEl.textContent = text;
        fileNameEl.classList.toggle('is-error', !!isError);
    }

    function attachCroppedImageToForm() {
        removeDynamicImageInput();
        const file = new File([croppedBlob], 'profile.jpg', { type: 'image/jpeg' });
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(file);
        dynamicImageInput = document.createElement('input');
        dynamicImageInput.type = 'file';
        dynamicImageInput.name = 'imageFile';
        dynamicImageInput.style.display = 'none';
        dynamicImageInput.files = dataTransfer.files;
        form.appendChild(dynamicImageInput);
        form.enctype = 'multipart/form-data';
    }

    function onFileSelected(file) {
        if (!file) {
            croppedBlob = null;
            removeDynamicImageInput();
            setFileName('No file selected', false);
            return;
        }
        if (file.size > maxBytes) {
            input.value = '';
            croppedBlob = null;
            removeDynamicImageInput();
            setFileName('File too large (max ' + Math.round(maxBytes / (1024 * 1024)) + ' MB)', true);
            return;
        }
        selectedFileName = file.name;
        croppedBlob = null;
        removeDynamicImageInput();
        setFileName('Adjust crop, then click “Use cropped image”', false);

        destroyCropper();
        cropSourceUrl = URL.createObjectURL(file);
        cropImg.src = cropSourceUrl;
        openModal();

        cropImg.onload = function () {
            cropImg.onload = null;
            if (cropper) {
                cropper.destroy();
                cropper = null;
            }
            cropper = new Cropper(cropImg, {
                aspectRatio: 1,
                viewMode: 1,
                dragMode: 'move',
                autoCropArea: 1,
                responsive: true,
                background: false,
                guides: true,
                center: true,
                highlight: true,
                cropBoxMovable: true,
                cropBoxResizable: true,
                toggleDragModeOnDblclick: false
            });
        };
    }

    chooseBtn.addEventListener('click', function () {
        input.click();
    });

    input.addEventListener('change', function () {
        onFileSelected(input.files[0]);
    });

    function onCropModalClosed() {
        destroyCropper();
        if (!croppedBlob) {
            input.value = '';
            setFileName('No file selected', false);
        }
    }

    if (cancelBtn) {
        cancelBtn.addEventListener('click', function () {
            croppedBlob = null;
            removeDynamicImageInput();
            onCropModalClosed();
        });
    }

    if (window.jQuery) {
        jQuery(modalEl).on('hidden.bs.modal', onCropModalClosed);
        jQuery(modalEl).on('shown.bs.modal', function () {
            if (cropper) {
                cropper.resize();
            }
        });
    }

    if (applyBtn) {
        applyBtn.addEventListener('click', function () {
            if (!cropper) {
                return;
            }
            const canvas = cropper.getCroppedCanvas({
                width: outputSize,
                height: outputSize,
                imageSmoothingEnabled: true,
                imageSmoothingQuality: 'high'
            });
            if (!canvas) {
                setFileName('Could not crop image. Try another file.', true);
                return;
            }
            canvas.toBlob(function (blob) {
                if (!blob) {
                    setFileName('Could not crop image. Try another file.', true);
                    return;
                }
                croppedBlob = blob;
                const previewUrl = URL.createObjectURL(blob);
                if (previewImg) {
                    if (previewImg.dataset.previewUrl) {
                        URL.revokeObjectURL(previewImg.dataset.previewUrl);
                    }
                    previewImg.src = previewUrl;
                    previewImg.dataset.previewUrl = previewUrl;
                }
                const baseName = selectedFileName.replace(/\.[^.]+$/, '') || 'profile';
                setFileName(baseName + '-cropped.jpg (will save on Save)', false);
                closeModal();
            }, 'image/jpeg', 0.92);
        });
    }

    form.addEventListener('submit', function (e) {
        if (!croppedBlob) {
            removeDynamicImageInput();
            return;
        }
        e.preventDefault();
        attachCroppedImageToForm();
        if (saveBtn) {
            saveBtn.disabled = true;
            saveBtn.textContent = 'Saving…';
        }
        form.submit();
    });
}
