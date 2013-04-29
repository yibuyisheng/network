define('ui/overlay',['thirdparty/jquery'],function(require, exports, module){

	  var $ = require('thirdparty/jquery');

	  var Overlay = function(options){

		  var self = this;
		  self.options = $.extend({

			  color: '#000',
			  opacity: 0.5,
			  zIndex: 99999,
			  effect: 'none',
			
			  onShow: null,
			  onHide: null,
			  closeOnClick:false,
			  lockScreen:false,
			  
			  parent:null
		  }, options||{});


		  $.extend(self,{
			_init:function(){ 
				self._render();		
			},
			_render:function(){
				
				var options = self.options;
				//start render
				var overlay  = $('<div></div>')
					  .addClass('overlay')
					  .css({
							background: options.color,
							opacity: options.opacity,
							top: 0,
							left: 0,
							width: $(window).width(),
							height: $(window).height(),
							position: 'fixed',
							zIndex: options.zIndex,
							display: 'none',
							overflow: 'hidden'
					    });
				

				if($.browser.msie&&$.browser.version=='6.0'){
					overlay.css('position','absolute');
					overlay.css('top',$(document).scrollTop());
					$(window).bind("scroll",function(){
						overlay.css('top',$(document).scrollTop());    
					});
				}
				
				
				if(options.closeOnClick) {
					  $(overlay).bind('click',function(){
							self.close();
					  });
				}
				//end render
				
				$('body').append(overlay);		
				$(window).bind("resize",function(){
					self._resize();
				});
			
				self.$elem = overlay;
			},
			_resize:function(){
				
				if(!self.$elem){
					return;
				}

				self.$elem.css({
					width: $(window).width(),
					height: $(window).height()
				});
			},
			show:function(){
				
				var options = self.options,
					overlay = self.$elem;

				switch(options.effect.toString().toLowerCase()) {
		
					  case 'fade':
						$(overlay).fadeIn('fast',options.onShow);
						break;	  
					  case 'slide':
						$(overlay).slideDown('fast',options.onShow);
						break;				
					  default:
						$(overlay).show(options.onShow);
						break;
					
				} 
					
				if(options.lockScreen){
					$('body').css('overflow', 'hidden');
				}

				return self;
			},
			close:function(){
				
				var options = self.options,
					overlay = self.$elem,
					parent = options.parent;
				
				if(!!options.parent && options.closeOnClick){
					options.parent = null;
					parent.close();
					return;
				}

				if(!self.$elem){
					return;
				}
				

				switch(options.effect.toString().toLowerCase()) {
			
					  case 'fade':
						$(overlay).fadeOut('fast', function() {
						  if(options.onHide) {
							options.onHide();
						  }
						  $(this).remove();
						});
						break;
							
					  case 'slide':
						$(overlay).slideUp('fast', function() {
						  if(options.onHide) {
							options.onHide();
						  }
						  $(this).remove();
						});
						break;
							
					  default:
						$(overlay).hide();
						if(options.onHide) {
						  options.onHide();
						}
						$(overlay).remove();
						break;
							
					}
					
					if(options.lockScreen){
						$('body').css('overflow', 'auto');
					}
					self.$elem = null;
			}
			
		  });

		  self._init();
		  return self;
	  }

	  module.exports = Overlay;
		
});