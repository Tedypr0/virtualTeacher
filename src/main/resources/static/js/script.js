/**
 * WEBSITE: https://themefisher.com
 * TWITTER: https://twitter.com/themefisher
 * FACEBOOK: https://www.facebook.com/themefisher
 * GITHUB: https://github.com/themefisher/
 */

//progress
$(document).ready(function() {

	// get box count
	var count = 0;
	var checked = 0;
	function countBoxes() {
		count = $("input[type='checkbox']").length;
		console.log(count);
	}

	countBoxes();
	$(":checkbox").click(countBoxes);

	// count checks

	function countChecked() {
		checked = $("input:checked").length;

		var percentage = parseInt(((checked / count) * 100),10);
		$(".progressbar-bar").progressbar({
			value: percentage
		});
		$(".progressbar-label").text(percentage + "%");
	}

	countChecked();
	$(":checkbox").click(countChecked);
});
//progress

(function ($) {
	'use strict';

	function hidePreloader() {
		$('.preloader').fadeOut(700);
	}

	// Hide preloader when page is ready; fallback if external assets block window "load"
	$(window).on('load', hidePreloader);
	$(document).ready(function () {
		setTimeout(hidePreloader, 2000);
	});

	// Sticky header: collapse utility + portal bars when scrolling down, hide on scroll down / show on scroll up
	$(document).ready(function () {
		var lastScrollTop = 0;
		var $header = $('header.header');

		function updateCompactHeader() {
			var scrollTop = $(window).scrollTop();

			// Compact mode (collapse top bar) once past 32px
			if (scrollTop > 32) {
				$header.addClass('header-compact');
				$('.navigation').addClass('nav-bg');
			} else {
				$header.removeClass('header-compact');
				$('.navigation').removeClass('nav-bg');
			}

			// Hide on scroll down, reveal on scroll up (only after 80px from top)
			if (scrollTop > 80) {
				if (scrollTop > lastScrollTop + 5) {
					$header.css('top', -$header.outerHeight() + 'px');
				} else if (scrollTop < lastScrollTop - 5) {
					$header.css('top', '0');
				}
			} else {
				$header.css('top', '0');
			}

			lastScrollTop = scrollTop;
		}

		$(window).on('scroll', updateCompactHeader);
		updateCompactHeader();
	});

	// navbarDropdown
	if ($(window).width() < 992) {
		$('.navigation .dropdown-toggle').on('click', function () {
			$(this).siblings('.dropdown-menu').animate({
				height: 'toggle'
			}, 300);
		});
	}

	// Background-images
	$('[data-background]').each(function () {
		var bg = $(this).data('background');
		if (bg && bg.indexOf('/') !== 0 && bg.indexOf('http') !== 0) {
			bg = '/' + bg;
		}
		$(this).css({
			'background-image': 'url(' + bg + ')'
		});
	});

	//Hero Slider
	if ($('.hero-slider').length) {
	$('.hero-slider').slick({
		autoplay: true,
		autoplaySpeed: 7500,
		pauseOnFocus: false,
		pauseOnHover: false,
		infinite: true,
		arrows: true,
		fade: true,
		prevArrow: '<button type=\'button\' class=\'prevArrow\'><i class=\'ti-angle-left\'></i></button>',
		nextArrow: '<button type=\'button\' class=\'nextArrow\'><i class=\'ti-angle-right\'></i></button>',
		dots: true
	});
	$('.hero-slider').slickAnimation();
	}

	// venobox popup
	$(document).ready(function () {
		$('.venobox').venobox();
	});


	// filter
	$(document).ready(function () {
		var containerEl = document.querySelector('.filtr-container');
		var filterizd;
		if (containerEl) {
			filterizd = $('.filtr-container').filterizr({});
		}
		//Active changer
		$('.filter-controls li').on('click', function () {
			$('.filter-controls li').removeClass('active');
			$(this).addClass('active');
		});
	});

	//  Count Up
	function counter() {
		var oTop;
		if ($('.count').length !== 0) {
			oTop = $('.count').offset().top - window.innerHeight;
		}
		if ($(window).scrollTop() > oTop) {
			$('.count').each(function () {
				var $this = $(this),
					countTo = $this.attr('data-count');
				$({
					countNum: $this.text()
				}).animate({
					countNum: countTo
				}, {
					duration: 1000,
					easing: 'swing',
					step: function () {
						$this.text(Math.floor(this.countNum));
					},
					complete: function () {
						$this.text(this.countNum);
					}
				});
			});
		}
	}
	$(window).on('scroll', function () {
		counter();
	});

})(jQuery);

// Teacher Application button logic (header only)
var modal = document.getElementById("myModal");
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];

if (btn && modal) {
	btn.onclick = function () {
		modal.style.display = "block";
	};
}

if (span && modal) {
	span.onclick = function () {
		modal.style.display = "none";
	};
}

if (modal) {
	window.onclick = function (event) {
		if (event.target === modal) {
			modal.style.display = "none";
		}
	};
}