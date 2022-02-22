# Change Log
All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).
 
## [Unreleased] - yyyy-mm-dd
 
Here we write upgrading notes for brands. It's a team effort to make them as
straightforward as possible.
 
### Added
- [RePol-issue-1](https://github.com/RCUB-Official/RePol/issues/1)
  MINOR Added parameters `service_name_the` and `service_owner_the` to support automatic inserting of a definite article before `service_name` and `service_owner` in _RePol Repository Policy Template_.
- MINOR Added parameters `service_name_the` and `service_owner_the` to support automatic inserting of a definite article before `service_name` and `service_owner` in _RePol Generic Privacy Policy Template_.
- [RePol-issue-3](https://github.com/RCUB-Official/RePol/issues/3)
  MINOR RePol freemarker templates moved to web archive.
- MAJOR Added changelog based on [teplate](https://gist.github.com/juampynr/4c18214a8eb554084e21d6e288a18a2c).
 
### Changed
- [RePol-issue-1](https://github.com/RCUB-Official/RePol/issues/1)
  MINOR _RePol Repository Policy Template_: Parameters `repo_name`, `repo_owner` and `repo_url` renamed to `service_name`, `service_owner` and `service_url`, respectively.
 
### Fixed
- [RePol-issue-3](https://github.com/RCUB-Official/RePol/issues/3)
  MAJOR RePol freemarker templates moved to web archive.
- PATCH fixed parsing and formatting of parameter `policy_date` in _RePol Generic Privacy Policy Template_.   
 
## [4.0.0] - 2022-01-04
Upgraded dependency versions and reinstalled the entire production server to use Java 11 and Tomcat 9 Servlet Container. Privacy Policy is now official and fully supported. UI and usability enhancements and changes in templates and input forms such as introductory and preparatory explanations, elaboration of reasons for data collection, children’s policy and further elaboration of cookies usages. Detailed RePol-based Privacy Policy and separate and much more detailed ToU, linked to the detailed survey "Feedback on NI4OS-Europe tools". Documentation: described detailed architecture and classes.

## [3.0.0] - 2021-06-03
Session is fully reorganized to cache requested forms, faster UI, and the entire workflow is reorganized to be more like a wizard with 4 steps. Introduced experimental Privacy Policy template. Documentation: described RePol purpose in Introduction and four steps in Usage.

## [2.0.0] - 2021-02-02
Forms definitions rendered as human-readable specifications, also allowing template and form configuration files to be downloaded for inspection. Applied EUPL1.2-or-later. Documented dependencies and their licenses, and elaborated support and handling of suggestions and template improvements proposals.

## [1.0.0] - 2020-12-01
Initial release containing only Repository Policy.
