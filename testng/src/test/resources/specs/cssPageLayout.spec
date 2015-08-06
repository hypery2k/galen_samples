@import shared/common.spec

@objects
  css-hint-*	div.bs-callout
	
# common layout checks
@import shared/commonLayout.spec

# concrete layout tests

= Code snippets should be vertical aligned =
css-hint-*:
    component shared/calloutComponent.spec