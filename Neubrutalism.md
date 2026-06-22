# Get rewarded for the internet you don't use
## Mission
Create implementation-ready, token-driven UI guidance for Get rewarded for the internet you don't use that is optimized for consistency, accessibility, and fast delivery across dashboard web app.
## Brand
- Product/brand: Get rewarded for the internet you don't use
- URL: https://www.grass.io/
- Audience: authenticated users and operators
- Product surface: dashboard web app
## Style Foundations
- Visual style: clean, functional, implementation-oriented
- Main font style: font.family.primary=DM Sans Variable, font.family.stack=DM Sans Variable, sans-serif, font.size.base=16px, font.weight.base=400, font.lineHeight.base=24px
- Typography scale: font.size.xs=9px, font.size.sm=11px, font.size.md=14px, font.size.lg=15.36px, font.size.xl=16px, font.size.2xl=18px, font.size.3xl=20px, font.size.4xl=24px
- Color palette: color.surface.base=#000000, color.text.secondary=#ffffff, color.text.tertiary=#111111, color.text.inverse=#1a202c, color.surface.raised=#abf600, color.surface.strong=#f2fed1, color.border.default=#e2e8f0
- Spacing scale: space.1=2px, space.2=4px, space.3=6px, space.4=8px, space.5=10px, space.6=14px, space.7=14.93px, space.8=16px
- Radius/shadow/motion tokens: radius.xs=7px, radius.sm=7.01px, radius.md=16px, radius.lg=40px, radius.xl=9999px | shadow.1=rgb(25, 26, 35) 0px 5px 0px 0px, shadow.2=rgb(25, 26, 35) 4px 4px 0px 0px | motion.duration.instant=150ms, motion.duration.fast=200ms, motion.duration.normal=300ms
## Accessibility
- Target: WCAG 2.2 AA
- Keyboard-first interactions required.
- Focus-visible rules required.
- Contrast constraints required.
## Writing Tone
Concise, confident, implementation-focused.
## Rules: Do
- Use semantic tokens, not raw hex values, in component guidance.
- Every component must define states for default, hover, focus-visible, active, disabled, loading, and error.
- Component behavior should specify responsive and edge-case handling.
- Interactive components must document keyboard, pointer, and touch behavior.
- Accessibility acceptance criteria must be testable in implementation.
## Rules: Don't
- Do not allow low-contrast text or hidden focus indicators.
- Do not introduce one-off spacing or typography exceptions.
- Do not use ambiguous labels or non-descriptive actions.
- Do not ship component guidance without explicit state rules.
## Guideline Authoring Workflow
1. Restate design intent in one sentence.
2. Define foundations and semantic tokens.
3. Define component anatomy, variants, interactions, and state behavior.
4. Add accessibility acceptance criteria with pass/fail checks.
5. Add anti-patterns, migration notes, and edge-case handling.
6. End with a QA checklist.
## Required Output Structure
- Context and goals.
- Design tokens and foundations.
- Component-level rules (anatomy, variants, states, responsive behavior).
- Accessibility requirements and testable acceptance criteria.
- Content and tone standards with examples.
- Anti-patterns and prohibited implementations.
- QA checklist.
## Component Rule Expectations
- Include keyboard, pointer, and touch behavior.
- Include spacing and typography token requirements.
- Include long-content, overflow, and empty-state handling.
- Include known page component density: links (34), buttons (30), cards (8), lists (1).
## Quality Gates
- Every non-negotiable rule must use "must".
- Every recommendation should use "should".
- Every accessibility rule must be testable in implementation.
- Teams should prefer system consistency over local visual exceptions.