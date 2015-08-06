= Overall layout =

  content:
    visible
  navbar:
   visible
  content:
    below navbar 370 to 410 px

  = navigation =
  
   @on mobile
     navigation-item-*:
       absent
  
   @on deskop
     navigation-item-*:
       visible  
      
  = Content should fit to screen size =
  
   @on mobile
     content:
       width 100% of screen/width
  
   @on deskop
     content:
       width 80 to 90% of screen/width 
    