(function () {
    'use strict';

    function openModal(name) {
        var modal = document.getElementById('modal-' + name);
        if (!modal) return;
        modal.removeAttribute('hidden');
        document.body.style.overflow = 'hidden';
    }

    function closeModal(name) {
        var modal = document.getElementById('modal-' + name);
        if (!modal) return;
        modal.setAttribute('hidden', 'hidden');
        document.body.style.overflow = '';
    }

    function bindModalTabs(modal) {
        var tabs  = modal.querySelectorAll('[data-tab]');
        var panes = modal.querySelectorAll('[data-pane]');
        tabs.forEach(function (tab) {
            tab.addEventListener('click', function () {
                var target = tab.getAttribute('data-tab');
                tabs.forEach(function (t) {
                    var active = t.getAttribute('data-tab') === target;
                    t.classList.toggle('is-active', active);
                    t.setAttribute('aria-selected', active ? 'true' : 'false');
                });
                panes.forEach(function (p) {
                    p.classList.toggle('is-active', p.getAttribute('data-pane') === target);
                });
            });
        });
    }

    function bindOpenTriggers() {
        document.querySelectorAll('[data-open-modal]').forEach(function (el) {
            el.addEventListener('click', function (e) {
                e.preventDefault();
                openModal(el.getAttribute('data-open-modal'));
            });
        });
    }

    function bindCloseTriggers() {
        document.querySelectorAll('[data-close-modal]').forEach(function (el) {
            el.addEventListener('click', function (e) {
                e.preventDefault();
                closeModal(el.getAttribute('data-close-modal'));
            });
        });
    }

    function bindEsc() {
        document.addEventListener('keydown', function (e) {
            if (e.key !== 'Escape') return;
            document.querySelectorAll('.modal:not([hidden])').forEach(function (m) {
                var id = m.id || '';
                if (id.indexOf('modal-') === 0) closeModal(id.substring(6));
            });
        });
    }

    function init() {
        document.querySelectorAll('.modal').forEach(bindModalTabs);
        bindOpenTriggers();
        bindCloseTriggers();
        bindEsc();
    }

    window.jakartaMarket = {
        openModal: openModal,
        closeModal: closeModal
    };

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
