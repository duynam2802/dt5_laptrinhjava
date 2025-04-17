// toast.js - Version tự động hoạt động
(function() {
    // Hàm hiển thị toast
    function showToast(message, duration = 3000, type = 'success') {
        const toast = document.createElement('div');
        toast.className = `toast-notification ${type}`;

        // Thêm style inline như là lớp bảo vệ cuối cùng
        toast.style.cssText = type === 'error' ?
            'font-size:14px !important; line-height:1.5 !important;' : '';

        toast.innerHTML = `
            <div class="message" style="font-size:inherit !important">${message}</div>
            <button class="close-btn">×</button>
            <div class="progress-bar"></div>
        `;
        document.body.appendChild(toast);

        // Hiển thị
        setTimeout(() => toast.classList.add('show'), 10);

        // Tự động ẩn
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, duration);

        // Nút đóng
        toast.querySelector('.close-btn').onclick = () => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        };
    }

    // Gán showToast vào window để có thể truy cập toàn cục
    window.showToast = showToast;

    // Tự động xử lý khi DOM tải xong
    document.addEventListener('DOMContentLoaded', function() {
        // Xử lý thông báo lỗi (error)
        const errorAlert = document.querySelector('.error-message');
        if (errorAlert && errorAlert.textContent.trim()) {
            showToast(errorAlert.textContent, 5000, 'error');
            errorAlert.style.display = 'none';
        }

        // Xử lý thông báo thành công (success)
        const successAlert = document.querySelector('.alert.alert-success');
        if (successAlert && successAlert.textContent.trim()) {
            showToast(successAlert.textContent, 3000, 'success');
            successAlert.style.display = 'none';
        }
    });
})();