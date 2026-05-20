/**
 * Profile image picker with square crop (Cropper.js).
 * Raw file may be large; uploaded image is resized to outputSize x outputSize JPEG.
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
    const maxBytes = options.maxBytes || 20 * 1024 * 1024;
    const outputSize = options.outputSize || 512;

    if (!input || !chooseBtn || !form || !modalEl || !cropImg) {
        return;
    }

    let cropper = null;
    let croppedBlob = null;
    let cropSourceUrl = null;
    let selectedFileName = '';

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

    function onFileSelected(file) {
        if (!file) {
            croppedBlob = null;
            setFileName('No file selected', false);
            return;
        }
        if (file.size > maxBytes) {
            input.value = '';
            croppedBlob = null;
            setFileName('File too large (max ' + Math.round(maxBytes / (1024 * 1024)) + ' MB)', true);
            return;
        }
        selectedFileName = file.name;
        croppedBlob = null;
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
                setFileName(baseName + '-cropped.jpg (ready to upload)', false);
                closeModal();
            }, 'image/jpeg', 0.92);
        });
    }

    form.addEventListener('submit', function (e) {
        if (!croppedBlob) {
            e.preventDefault();
            setFileName('Choose an image and apply crop before uploading.', true);
            return;
        }
        e.preventDefault();

        const formData = new FormData();
        formData.append('imageFile', croppedBlob, 'profile.jpg');

        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.textContent = 'Uploading…';
        }

        fetch(form.action, {
            method: 'POST',
            body: formData,
            redirect: 'follow',
            credentials: 'same-origin'
        })
            .then(function (response) {
                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }
                if (!response.ok) {
                    throw new Error('Upload failed');
                }
                window.location.reload();
            })
            .catch(function () {
                setFileName('Upload failed. Try again.', true);
                if (submitBtn) {
                    submitBtn.disabled = false;
                    submitBtn.textContent = 'Upload image';
                }
            });
    });
}
