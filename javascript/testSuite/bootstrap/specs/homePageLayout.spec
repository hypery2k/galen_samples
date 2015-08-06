@import shared/common.spec

@objects
  bootstrap-logo		span.bs-docs-booticon
  
# common layout checks
@import shared/commonLayout.spec

# concrete layout tests

= Bootstrap logo should be visible =

  bootstrap-logo:
    visible