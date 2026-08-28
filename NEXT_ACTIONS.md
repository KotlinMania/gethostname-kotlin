# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/1 (100.0%)
- **Function parity:** 1/3 matched (target 1) — 33.3%
- **Class/type parity:** 0/0 matched (target 1) — N/A
- **Combined symbol parity:** 1/3 matched (target 2) — 33.3%
- **Average inline-code cosine:** 0.22 (function body across 1 matched files)
- **Average documentation cosine:** 0.94 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `gethostname.Gethostname [PROVENANCE-FALLBACK]`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 20307.8
- **Functions:** 1/3 matched (target 1)
- **Missing functions:** `gethostname`, `get_computer_physical_dns_hostname`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 1/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

