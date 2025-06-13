class ThemeManager {
    constructor() {
        this.theme = localStorage.getItem('theme') || 'light';
        this.init();
    }

    init() {
        this.applyTheme();
        this.bindEvents();
    }

    applyTheme() {
        document.documentElement.setAttribute('data-theme', this.theme);
        const themeToggle = document.getElementById('theme-toggle');
        const icon = themeToggle.querySelector('i');
        icon.className = this.theme === 'dark' ? 'fas fa-music' : 'fas fa-play';
    }

    toggleTheme() {
        this.theme = this.theme === 'light' ? 'dark' : 'light';
        localStorage.setItem('theme', this.theme);
        this.applyTheme();
    }

    bindEvents() {
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.addEventListener('click', () => this.toggleTheme());
        }
    }
}

class EditManager {
    constructor() {
        this.isEditMode = false;
        this.init();
    }

    init() {
        this.loadData();
        this.bindEvents();
    }

    toggleEditMode() {
        this.isEditMode = !this.isEditMode;
        const editToggle = document.getElementById('edit-toggle');
        const editableElements = document.querySelectorAll('[data-field]');
        const editControls = document.querySelectorAll('.edit-controls');

        if (this.isEditMode) {
            editToggle.classList.add('active');
            editableElements.forEach(el => {
                el.contentEditable = true;
                el.style.cursor = 'text';
            });
            editControls.forEach(control => {
                control.style.display = 'block';
            });
        } else {
            editToggle.classList.remove('active');
            editableElements.forEach(el => {
                el.contentEditable = false;
                el.style.cursor = 'default';
            });
            editControls.forEach(control => {
                control.style.display = 'none';
            });
            this.saveData();
        }
    }

    saveData() {
        const data = {};
        document.querySelectorAll('[data-field]').forEach(el => {
            data[el.dataset.field] = el.textContent.trim();
        });
        localStorage.setItem('cvData', JSON.stringify(data));
    }

    loadData() {
        const savedData = localStorage.getItem('cvData');
        if (savedData) {
            const data = JSON.parse(savedData);
            Object.keys(data).forEach(key => {
                const element = document.querySelector(`[data-field="${key}"]`);
                if (element) {
                    element.textContent = data[key];
                }
            });
        }
    }

    bindEvents() {
        const editToggle = document.getElementById('edit-toggle');
        if (editToggle) {
            editToggle.addEventListener('click', () => this.toggleEditMode());
        }
    }
}

class NavigationManager {
    constructor() {
        this.init();
    }

    init() {
        this.bindEvents();
        this.handleScroll();
    }

    bindEvents() {
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const targetId = link.getAttribute('href').substring(1);
                this.scrollToSection(targetId);
                this.setActiveLink(link);
            });
        });

        window.addEventListener('scroll', () => this.handleScroll());
    }

    scrollToSection(sectionId) {
        const section = document.getElementById(sectionId);
        if (section) {
            const offsetTop = section.offsetTop - 80;
            window.scrollTo({
                top: offsetTop,
                behavior: 'smooth'
            });
        }
    }

    setActiveLink(activeLink) {
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        activeLink.classList.add('active');
    }

    handleScroll() {
        const sections = document.querySelectorAll('.section, .hero');
        const scrollPos = window.scrollY + 100;

        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.offsetHeight;
            const sectionId = section.getAttribute('id');

            if (scrollPos >= sectionTop && scrollPos < sectionTop + sectionHeight) {
                const correspondingLink = document.querySelector(`[data-section="${sectionId}"]`);
                if (correspondingLink) {
                    this.setActiveLink(correspondingLink);
                }
            }
        });
    }
}

class SkillsManager {
    constructor() {
        this.init();
    }

    init() {
        this.animateSkillBars();
        this.bindSliderEvents();
    }

    animateSkillBars() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const skillBars = entry.target.querySelectorAll('.skill-progress');
                    skillBars.forEach(bar => {
                        const width = bar.dataset.width || bar.style.width;
                        bar.style.width = '0%';
                        setTimeout(() => {
                            bar.style.width = width;
                        }, 100);
                    });
                }
            });
        }, { threshold: 0.1 });

        document.querySelectorAll('.skills-card').forEach(card => {
            observer.observe(card);
        });
    }

    bindSliderEvents() {
        document.querySelectorAll('.skill-slider').forEach(slider => {
            slider.addEventListener('input', (e) => {
                const skillName = e.target.dataset.skill;
                const value = e.target.value;
                const progressBar = document.querySelector(`[data-skill="${skillName}"]`);
                const percentageSpan = progressBar.closest('.skill-item').querySelector('.skill-percentage');
                
                progressBar.style.width = `${value}%`;
                progressBar.dataset.width = `${value}%`;
                percentageSpan.textContent = `${value}%`;
                percentageSpan.dataset.field = `skill-${skillName}-value`;
            });
        });
    }
}

class ParticlesManager {
    constructor() {
        this.init();
    }

    init() {
        this.createParticles();
    }

    createParticles() {
        const particlesContainer = document.querySelector('.floating-particles');
        if (!particlesContainer) return;
        const particleCount = 15;

        for (let i = 0; i < particleCount; i++) {
            const particle = document.createElement('i');
            particle.className = 'fas fa-music particle';
            particle.style.cssText = `
                position: absolute;
                font-size: ${Math.random() * 12 + 8}px;
                color: #FE2C55;
                opacity: ${Math.random() * 0.5 + 0.3};
                left: ${Math.random() * 100}%;
                top: ${Math.random() * 100}%;
                animation: floatParticle ${Math.random() * 8 + 4}s ease-in-out infinite;
                animation-delay: ${Math.random() * 5}s;
            `;
            particlesContainer.appendChild(particle);
        }
    }
}

class ProfileManager {
    constructor() {
        this.init();
    }

    init() {
        this.bindImageUpload();
    }

    bindImageUpload() {
        const profileImg = document.getElementById('profile-img');
        if (!profileImg) return;
        
        profileImg.addEventListener('click', () => {
            if (document.querySelector('#edit-toggle').classList.contains('active')) {
                const input = document.createElement('input');
                input.type = 'file';
                input.accept = 'image/*';
                input.onchange = (e) => {
                    const file = e.target.files[0];
                    if (file) {
                        const reader = new FileReader();
                        reader.onload = (e) => {
                            profileImg.src = e.target.result;
                            localStorage.setItem('profileImage', e.target.result);
                        };
                        reader.readAsDataURL(file);
                    }
                };
                input.click();
            }
        });

        const savedImage = localStorage.getItem('profileImage');
        if (savedImage) {
            profileImg.src = savedImage;
        }
    }
}

class AnimationManager {
    constructor() {
        this.init();
    }

    init() {
        this.observeElements();
    }

    observeElements() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                }
            });
        }, {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        });

        document.querySelectorAll('.about-card, .career-card, .skills-card, .project-card, .contact-info, .social-links, .certificates-section').forEach(el => {
            el.classList.add('animate-out');
            observer.observe(el);
        });
    }
}

function downloadCV() {
    const cvData = {
        name: document.querySelector('[data-field="name"]')?.textContent || 'Hà Phạm Anh Huy',
        phone: document.querySelector('[data-field="phone"]')?.textContent || '0385190557',
        email: document.querySelector('[data-field="email"]')?.textContent || 'haphamanhhuy123@gmail.com'
    };
    
    const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(cvData, null, 2));
    const downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", "CV_Ha_Pham_Anh_Huy.json");
    document.body.appendChild(downloadAnchorNode);
    downloadAnchorNode.click();
    downloadAnchorNode.remove();
}

document.addEventListener('DOMContentLoaded', () => {
    new ThemeManager();
    new EditManager();
    new NavigationManager();
    new SkillsManager();
    new ParticlesManager();
    new ProfileManager();
    new AnimationManager();

    const downloadBtn = document.querySelector('.btn-primary');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', downloadCV);
    }

    // Interactive effects
    document.querySelectorAll('.btn-primary, .btn-secondary, .social-link, .project-link').forEach(el => {
        el.addEventListener('mouseenter', () => {
            el.classList.add('bounce');
        });
        el.addEventListener('mouseleave', () => {
            el.classList.remove('bounce');
        });
    });

    document.querySelectorAll('.about-card, .skills-card, .project-card').forEach(card => {
        card.addEventListener('click', function(e) {
            const ripple = document.createElement('div');
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = e.clientX - rect.left - size / 2;
            const y = e.clientY - rect.top - size / 2;
            
            ripple.style.cssText = `
                position: absolute;
                width: ${size}px;
                height: ${size}px;
                left: ${x}px;
                top: ${y}px;
                background: rgba(254, 44, 85, 0.3);
                border-radius: 50%;
                transform: scale(0);
                animation: ripple 0.6s ease-out;
                pointer-events: none;
            `;
            
            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);
            
            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });

    const style = document.createElement('style');
    style.textContent = `
        @keyframes ripple {
            to {
                transform: scale(2);
                opacity: 0;
            }
        }
        @keyframes floatParticle {
            0%, 100% { 
                transform: translateY(0px) translateX(0px) rotate(0deg);
                opacity: 0.4;
            }
            25% { 
                transform: translateY(-15px) translateX(10px) rotate(90deg);
                opacity: 0.8;
            }
            50% { 
                transform: translateY(-10px) translateX(-10px) rotate(180deg);
                opacity: 0.6;
            }
            75% { 
                transform: translateY(-20px) translateX(5px) rotate(270deg);
                opacity: 0.9;
            }
        }
        @keyframes bounce {
            0%, 100% { transform: translateY(0) scale(1); }
            50% { transform: translateY(-5px) scale(1.05); }
        }
    `;
    document.head.appendChild(style);
});