@import shared/common.spec

@objects
  js-code-snippet-*		div.bs-callout
  ad-container			#carbonads-container
  ad					.carbonad

# common layout checks
@import shared/commonLayout.spec

# concrete layout tests

= Code snippets should be vertical aligned =

  @forEach [js-code-snippet-*] as snippet, next as nextSnippet
    ${snippet}:
      aligned vertically left ${nextSnippet}
    
= Ad should be centered =

  ad:
    centered horizontally on ad-container
