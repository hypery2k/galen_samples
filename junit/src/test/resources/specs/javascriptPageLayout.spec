@import shared/common.spec

@objects
  js-code-snippet-*		div.bs-callout
  ad-container			#carbonads-container
  ad					.carbonad

# common layout checks
@import shared/commonLayout.spec

# concrete layout tests

= Code snippets should be vertical aligned =

  js-code-snippet-*:
    aligned vertically left js-code-snippet-1
    
= Ad should be centered =

  ad:
    centered horizontally on ad-container
