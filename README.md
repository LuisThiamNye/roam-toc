# roam-toc
A roam/render component that automatically generates a table of contents for the current page.

When clicking a heading, it scrolls to the location of the block instead of focusing on the block.

Detailed instructions at RoamStack: [Add a table of contents to your pages with roam-toc](https://roamstack.com/kb/extensions/roam-toc/)

![Screenshot 2021-03-09 at 21 00 47](https://user-images.githubusercontent.com/41270840/110537409-8c6cb780-811a-11eb-80db-f58fce4fe3cb.png)

**Usage:**
`{{[[roam/render]]: ((ref-to-clojure-codeblock))}}`

The code is in toc.cljs

I would recommend saving this as a template with #roam/templates

Some current limitations:
- The heading must be visible on the page in order to jump to it. (The script could be improved by automatically expanding blocks as necessary.)

## Changelog:
- 2021-05-14: Supports rendering markdown:
![Screenshot 2021-05-14 at 17 11 40](https://user-images.githubusercontent.com/41270840/118298705-b99d6380-b4d7-11eb-9d28-8281200b78cf.png)
