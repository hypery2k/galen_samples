
=====================================

header          css #header
navigation      css #navigation
main            css #main-container
banner-panel    css #banner-panel
footer          css #footer

=====================================


@ *
--------------------
header
    inside: screen 0px top left right
    height: 60px

navigation
    inside: screen 0px left
    below: header 0px

footer
    inside: screen 0px bottom left right
    below: banner-panel 0px
    height: 100px



@ desktop
------------------
navigation
    width: 300px
    aligned horizontally all: main
    near: main 0px left

main
    below: header 0px



@ desktop, tablet
-------------------
banner-panel
    below: header 0px
    inside: screen 0px right
    width: 300px
    near: main 0px right
    aligned horizontally all: main



@ tablet
-----------------
navigation
    inside: screen 0px left right
    above: main 0px
    width: 60px

main
    inside: screen 0px left



@ mobile
----------------------
navigation
    inside: screen 0px left right
    above: main 0px
    height: > 60px

main
    inside: screen 0px left right
    above: banner-panel 0px

banner-panel
    inside: screen 0px left right
    height: > 50px


