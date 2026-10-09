/* ============================================================
   College Seminar Portal — JavaScript
   
   Plain JavaScript (no frameworks or libraries).
   Provides:
   - Mobile navigation toggle
   - Client-side form validation (complements server-side)
   - Confirmation dialogs for delete actions
   - Auto-hide alert messages
   - Character count for textareas
   ============================================================ */

// Run after the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {

    // ---- 1. Mobile Navigation Toggle ---------------------
    const toggle = document.getElementById('navToggle');
    const links  = document.getElementById('navLinks');
    if (toggle && links) {
        toggle.addEventListener('click', function () {
            links.classList.toggle('open');
        });
    }

    // ---- 2. Auto-hide Alert Messages after 5 seconds -----
    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(function () { alert.style.display = 'none'; }, 500);
        }, 5000);
    });

    // ---- 3. Delete Confirmation --------------------------
    // Any element with class "confirm-delete" will show a
    // browser confirm() dialog before proceeding.
    document.querySelectorAll('.confirm-delete').forEach(function (el) {
        el.addEventListener('click', function (e) {
            var msg = el.getAttribute('data-confirm') ||
                      'Are you sure you want to delete this? This action cannot be undone.';
            if (!confirm(msg)) {
                e.preventDefault(); // cancel if user clicks Cancel
            }
        });
    });

    // ---- 4. Client-side Registration Form Validation -----
    var regForm = document.getElementById('registrationForm');
    if (regForm) {
        regForm.addEventListener('submit', function (e) {
            var name  = document.getElementById('fullName');
            var email = document.getElementById('email');
            var roll  = document.getElementById('rollNumber');
            var dept  = document.getElementById('department');

            clearErrors();

            var hasError = false;

            if (!name || name.value.trim() === '') {
                showError(name, 'Full name is required.');
                hasError = true;
            }

            if (email) {
                var emailVal = email.value.trim();
                if (emailVal === '') {
                    showError(email, 'Email address is required.');
                    hasError = true;
                } else if (!/^[\w.+\-]+@[\w\-]+\.[\w.]{2,}$/.test(emailVal)) {
                    showError(email, 'Please enter a valid email address.');
                    hasError = true;
                }
            }

            if (!roll || roll.value.trim() === '') {
                showError(roll, 'Roll number is required.');
                hasError = true;
            }

            if (!dept || dept.value.trim() === '') {
                showError(dept, 'Department is required.');
                hasError = true;
            }

            if (hasError) e.preventDefault();
        });
    }

    // ---- 5. Admin Event Form Validation ------------------
    var eventForm = document.getElementById('eventForm');
    if (eventForm) {
        eventForm.addEventListener('submit', function (e) {
            clearErrors();
            var hasError = false;
            var required = ['title', 'eventType', 'department', 'eventDate',
                            'eventTime', 'venue', 'speakerName', 'totalSeats'];
            required.forEach(function (id) {
                var el = document.getElementById(id);
                if (el && el.value.trim() === '') {
                    showError(el, 'This field is required.');
                    hasError = true;
                }
            });

            var seats = document.getElementById('totalSeats');
            if (seats && parseInt(seats.value) <= 0) {
                showError(seats, 'Total seats must be a positive number.');
                hasError = true;
            }

            if (hasError) e.preventDefault();
        });
    }

    // ---- 6. Character Counter for Textarea ---------------
    document.querySelectorAll('textarea[maxlength]').forEach(function (ta) {
        var maxLen  = parseInt(ta.getAttribute('maxlength'));
        var counter = document.createElement('div');
        counter.className = 'form-hint char-counter';
        counter.textContent = '0 / ' + maxLen + ' characters';
        ta.parentNode.appendChild(counter);

        ta.addEventListener('input', function () {
            counter.textContent = ta.value.length + ' / ' + maxLen + ' characters';
        });
    });

    // ---- 7. Highlight current nav link -------------------
    var current = window.location.pathname;
    document.querySelectorAll('.navbar-links a').forEach(function (a) {
        if (a.getAttribute('href') && current.endsWith(a.getAttribute('href').replace(/\?.*/, ''))) {
            a.classList.add('active');
        }
    });
});

// ---- Helper Functions ------------------------------------

function showError(input, message) {
    if (!input) return;
    input.style.borderColor = '#dc2626';
    var errDiv = document.createElement('div');
    errDiv.className = 'form-error-msg';
    errDiv.style.cssText = 'color:#dc2626;font-size:0.78rem;margin-top:3px;';
    errDiv.textContent = message;
    input.parentNode.appendChild(errDiv);
}

function clearErrors() {
    document.querySelectorAll('.form-error-msg').forEach(function (el) { el.remove(); });
    document.querySelectorAll('input, select, textarea').forEach(function (el) {
        el.style.borderColor = '';
    });
}
