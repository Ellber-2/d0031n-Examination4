/* Lightbox för Hämta Schema */
document.getElementById('fetchScheduleBtn').addEventListener('click', function() {
    const lightbox = document.getElementById('lightbox');
    const lightboxContent = document.querySelector('.lightbox-content');
    lightbox.style.display = 'block';
    document.body.style.overflow = 'hidden';
    setTimeout(() => {
        lightbox.classList.add('show');
        lightboxContent.classList.add('show');
    }, 10);
});

document.querySelectorAll('.close').forEach(closeBtn => {
    closeBtn.addEventListener('click', function() {
        const lightbox = this.closest('.lightbox');
        const lightboxContent = lightbox.querySelector('.lightbox-content');
        lightbox.classList.remove('show');
        lightboxContent.classList.remove('show');
        setTimeout(() => {
            lightbox.style.display = 'none';
            document.body.style.overflow = 'auto';
        }, 500);
    });
});

// Prevent negative numbers in number inputs and limit length to 3 digits
document.querySelectorAll('input[type="number"]').forEach(input => {
    input.addEventListener('keypress', function(event) {
        if (event.key === '-' || this.value.length >= 3) {
            event.preventDefault();
        }
    });
    input.addEventListener('input', function() {
        if (this.value.includes('-')) {
            this.value = this.value.replace('-', '');
        }
        if (this.value.length > 3) {
            this.value = this.value.slice(0, 3);
        }
    });
});

// Convert form data to JSON and store in a cookie
document.getElementById('convertToJsonBtn').addEventListener('click', function() {
    const form = document.getElementById('scheduleForm');
    const formData = new FormData(form);
    const jsonObject = {};
    formData.forEach((value, key) => {
        if (key === 'labbsalar' || key === 'distans') {
            jsonObject[key] = form.elements[key].checked;
        } else {
            jsonObject[key] = value;
        }
    });
    const jsonString = JSON.stringify(jsonObject);
    document.cookie = `formData=${encodeURIComponent(jsonString)}; path=/; max-age=3600`; // Store cookie for 1 hour
    console.log(jsonString); // Print JSON to console
});

// Function to get cookie value by name
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

// Show JSON data in a new lightbox
document.getElementById('showJsonBtn').addEventListener('click', function() {
    const storedJson = getCookie('formData');
    if (storedJson) {
        const parsedData = JSON.parse(decodeURIComponent(storedJson));
        document.getElementById('jsonData').textContent = JSON.stringify(parsedData, null, 2);
        const jsonLightbox = document.getElementById('jsonLightbox');
        const jsonLightboxContent = jsonLightbox.querySelector('.lightbox-content');
        jsonLightbox.style.display = 'block';
        document.body.style.overflow = 'hidden';
        setTimeout(() => {
            jsonLightbox.classList.add('show');
            jsonLightboxContent.classList.add('show');
        }, 10);
    } else {
        alert('No JSON data found in cookies.');
    }
});