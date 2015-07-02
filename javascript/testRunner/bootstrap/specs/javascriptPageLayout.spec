@@ import shared/common.spec

==============================================================
js-code-snippet-*      	css    	div.bs-callout
ad-container	    	id    	carbonads-container
ad	    				css    	.carbonad
==============================================================
# common layout checks
@@ import shared/commonLayout.spec

# concrete layout tests

@ Code snippets should be vertical aligned | *
--------------------------
[ 1 - ${count("js-code-snippet-*")-1} ]
js-code-snippet-*
    aligned vertically left: js-code-snippet-@{+1}
    
@ Ad should be centered  | *
--------------------------
ad
    centered horizontally on: ad-container
