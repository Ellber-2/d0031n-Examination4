/* Lightbox för Hämta Schema */
document.getElementById('fetchScheduleBtn').addEventListener('click', function() {
    document.getElementById('lightbox').style.display = 'block';
});

document.querySelector('.close').addEventListener('click', function() {
    document.getElementById('lightbox').style.display = 'none';
});