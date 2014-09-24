@@ import shared/common.spec

==============================================================
app-entry-*         css     a.list-group-item 
app-entry-logo-*    css     a.list-group-item div.pull-left i.fa

==============================================================
# common layout checks
@@ import shared/commonLayout.spec

# concrete layout tests

@ Each app entry should have a logo| *
[ 1 - 4 ]
app-entry-logo-@
    near: app-entry-@ -79 px left