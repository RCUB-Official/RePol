${embedded_data?no_esc}

<html>
            <head>
                        <title>Repository Policy - <#if service_name_the>the </#if>${service_name}</title>
        <style>
}
            .red-strong {
                font-weight:bold;
}
        </style>
            </head>
            <body>
 
<h1>Repository Policy - <#if service_name_the>the </#if>${service_name}</h1>

<div class="generated_header">
            <p>Date (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}</p>
</div>
 
<!-- PREAMBLE -->
 
<#if include_preamble>
<h2>About <#if service_name_the>the </#if>${service_name}</h2>
 
<p><span class="red-strong">TODO: TO REVIEW AND EDIT BY POLICY WRITER, (SEARCH FOR PARAGRAPHS WITH &quot;TODO:&quot;)</span></p>
<p><span class="red-strong">TODO: </span><#if service_owner_the>the </#if>${service_owner} has established the ${repo_type} repository <#if service_url!=""><a target="_blank" href="${service_url}"><#if service_name_the>the </#if>${service_name} (${service_url})</a><#else><#if service_name_the>the </#if>${service_name})</#if>. It is the digital repository of ALL (OR THOSE QUALIFIED/CONSTRAINED WITH, E.G., “research”) MEMBERS OR DEPARTMENTS OR DEPOSITING/ASSOCIATED INSTITUTIONS of <#if service_owner_the>the </#if>${service_owner}<#if parent_organization!=""> OR ${parent_organization}</#if>.</p>
<p><span class="red-strong">TODO: </span>The aim of the repository is to provide open access to DIGITAL COLLECTIONS OR RESEARCH OUTPUTS OR SCHOLARLY OUTPUTS of <#if service_owner_the>the </#if>${service_owner} IN SOME SPECIFIC FIELD (IN LINE WITH THE REPOSITORY TYPE (${repo_type}) AND SUBJECTS<#if repo_subjects?has_content> (<#list repo_subjects as element>${element}<#if element?has_next>, </#if></#list>)</#if>). The main aim of this repository is to increase the visibility of its <#if item_types_overally=="educational materials">teaching and </#if>research output / OR of the MEMBERS OR DEPARTMENTS<#if allowed_depositors?has_content> OR ALLOWED DEPOSITORS (<#list allowed_depositors as element>${element}<#if element?has_next>, </#if></#list>)</#if> of <#if service_owner_the>the </#if>${service_owner}<#if parent_organization!=""> OR ${parent_organization}</#if> and, at the same time, provide free access, in compliance with the <#if oa_policy_url!=""><a target="_blank" href="${oa_policy_url}"> Open Access/Science Policy (${oa_policy_url })</a><#else>LINK TO Open Access/Science Policy (ALSO PRINTABLE URL)</#if> of <#if service_owner_the>the </#if>${service_owner}<#if parent_organization!=""> OR ${parent_organization}</#if> passed by the GOVERNING BODY (DATE OF POLICY, E.G., 1 April 2020).</p>
<p><span class="red-strong">TODO: </span>The aim of this policy is to ensure that the contents of <#if service_name_the>the </#if>${service_name} reflect and strengthen the <#if service_owner_the>the </#if>${service_owner}’s<#if parent_organization!=""> OR ${parent_organization}&apos;s</#if> tradition of research excellence, by increasing the visibility of <#if service_owner_the>the </#if>${service_owner}’s research, and providing open access to its research outputs. This policy also establishes the framework within which <#if service_name_the>the </#if>${service_name} is managed.</p>
<p><span class="red-strong">TODO: </span><span class="red">DESCRIPTION OF REPOSITORY PLATFORM AND VENDOR; EXAMPLE: </span>The repository uses a <a target="_blank" href="https://github.com/DSpace/DSpace">DSpace</a>-based software platform developed and maintained by the MAINTAINER/SUPPORTER (WITH LINK AND PRINTABLE URL). The software platform is compliant with the <a target="_blank" href="https://guidelines.openaire.eu/en/latest/literature/index_guidelines-lit_v3.html ">OpenAIRE Guidelines for Literature Repositories v3</a>.</p>
<p><span class="red-strong">TODO: </span><span class="red">DESCRIPTION OF FRONT-END APP SUPPORTING DEPOSITORS OR OTHER USERS, EXAMPLE: </span>The repository is supplied with the external application FRONT END APP NAME (LINK AND PRINTABLE URL), developed by the SOLUTION DEVELOPER (LINK AND PRINTABLE URL). In FRONT-END APP, authors and funding information are presented as normalized entities. FRONT-END APP also enables metadata export and displays <a target="_blank" href="https://www.altmetric.com/">Altmetric scores</a> and <a target="_blank" href="https://www.dimensions.ai/">Dimensions</a> citations.</p>
<p>Related documents:</p>
<ul>
            <#if include_cc_explainer>
            <li><a target="_self" href="#AboutCC">Creative Commons Licences Explainer (below) </a></li>
            </#if>
            <#if manual_url!="">
            <li><a target="_blank" href="${manual_url}">User Manual (${manual_url})</a></li>
            <#else>
            <li><span class="red-strong">TODO: </span>OPTIONAL LINK TO User Manual (ALSO PRINTABLE URL)</li>
            </#if>
            <#if oa_policy_url!="">
            <li><a target="_blank" href="${oa_policy_url}">Open Access/Science Policy (${oa_policy_url })</a></li>
            <#else>
            <li><span class="red-strong">TODO: </span>LINK TO Open Access/Science Policy (ALSO PRINTABLE URL)</li>
            </#if>
</ul>
</#if>
 
<!-- DESIGNATED COMMUNITY -->
 
<h2><span class=”red-strong”>TODO: </span>Designated Community</h2>
<span class=”red”>Describe briefly the groups of current and potential users and communities for whom the content of the repository is relevant. The designated community may include various sub-communities and it may be relevant to distinguish between primary and secondary communities or internal and external users. For example:</span>
<p>The Designated Community of <#if service_name_the>the </#if>${service_name} includes the following user groups:</p>
<ul>
            <li>Internal users: MEMBERS, STAFF OR FACULTY of <#if service_owner_the>the </#if>${service_owner}<#if parent_organization!=""> OR ${parent_organization}</#if>. They can deposit their research outputs in the repository, browse and search the metadata, and, in most cases, download content from all repository collections.</li>
            <li>External users: local and international professional researchers, citizen scientists, teachers, students, informed citizens, the general audience, etc. They can browse and search the metadata, and download the Open Access content from the repository.</li>
            <li>Associates: local and international professional researchers who have joint projects with internal users. They can browse and search the metadata, download Open Access content from the repository, access the collection dedicated to their project.</li>
</ul>
 
<!-- METADATA POLICY -->
 
<h2>Metadata Policy</h2>
 
<ul>
<#if restricted_access_some_metadata>
            <li>Access to some metadata is restricted.</li>
<#else>
            <li>Anyone may access the metadata free of charge.</li>
</#if>
 
<#if metadata_policy_flavour=="cc0" || metadata_policy_flavour=="cc-by">
            <li>The metadata of the items deposited in <#if service_name_the>the </#if>${service_name} are made available under the Creative Commons <#if metadata_policy_flavour=="cc0"><a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/">CC0 Public Domain Dedication option (“no rights reserved", https://creativecommons.org/publicdomain/zero/1.0/)</a>. This dedication<#else><a target="_blank" href="https://creativecommons.org/licenses/by/4.0/">Attribution CC BY (Attribution Only)</a> licence. This licence</#if> applies exclusively to the metadata associated with the items deposited and under no circumstances is a substitute for or affects their individual licences or permits.</li>
            <li>Anyone can access, copy, share, modify, harvest and re-use the metadata for any purpose, free of charge and without requiring permission from <#if service_owner_the>the </#if>${service_owner}<#if metadata_policy_flavour=="cc-by">, as long as the original source of metadata of is cited</#if>.</li>
<#else><!-- descriptive (OpenDOAR) -->
            <#if commercial_metadata_use_allowed><!-- Minimum -->
            <li>The metadata may be re-used in any medium without prior permission for not-for-profit purposes provided:</li>
            <#else><!-- Optimum: commercial metadata use -->
            <li>The metadata may be re-used in any medium without prior permission for not-for-profit purposes and re-sold commercially provided:
            </#if>
                        <ul>
                                    <li>The OAI Identifier or a link to the original metadata record is given</li>
                        <#if mention_repository_owner>
                                    <li><#if service_owner_the>The </#if>${service_owner} is mentioned</li>
                        </#if>
                        </ul>
            </li>
</#if>
            <li><#if service_owner_the>The </#if>${service_owner} is dedicated to providing high-quality and machine-readable item-level metadata.</li>
            <li>Mention of <#if service_name_the>the </#if>${service_name} is appreciated but not mandatory.</li>
</ul>
 
<!-- DATA POLICY -->
 
<h2>Data Policy</h2>
 
<ul>
<#if restricted_access_some_items>
            <li>Access to some items is restricted.</li>
<#else>
            <li>Anyone may access items free of charge.</li>
</#if>
            <li>All items are individually tagged with appropriate permissions and terms of use.</li>
 
<#if data_policy_flavour_cc>
            <li>The application of open licences, preferably the Creative Commons <a target="_blank" href="https://creativecommons.org/licenses/by/4.0/">CC BY (Attribution)</a> licence, is recommended. This is at the discretion of the author and depends on funder’s requirements<#if includes_publications> or publishing arrangements</#if>, and licensing requirements of input content. In some instances,
<#if mention_cc_by_sa>
<a target="_blank" href="https://creativecommons.org/licenses/by-sa/4.0/">CC BY-SA (Attribution + Share-alike)</a>,
</#if>
<#if mention_cc_by_nc>
<a target="_blank" href="https://creativecommons.org/licenses/by-nc/4.0/">CC BY-NC (Attribution + No Commercial Use)</a>,
</#if>
<#if mention_cc_by_nc_sa>
<a target="_blank" href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA (Attribution + No Commercial Use + Share-alike)</a>,
</#if>
<#if mention_cc_by_sa || mention_cc_by_nc || mention_cc_by_nc_sa>or </#if>
<a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/">CC0 Public Domain Dedication option (“no rights reserved")</a> may be more appropriate.<#if includes_software> When software is deposited, the applied commonly-used software licence should be indicated.</#if></li>
            <li>The applied licence is explicitly indicated in item metadata.</li>
            <#if includes_publications>
            <li>Publication metadata must contain full bibliographic information including DOI or other persistent identifiers.</li>
            </#if>
            <li>The licence and access status  of the items will be made machine-readable per state of art conventions, guidelines and relevant and widely applied standards. </li>
 
<#else><!-- descriptive (OpenDOAR) -->
            <#if opendoar_minimum>
            <li>Single copies of data can be:
                        <ul>
                                    <li>reproduced and displayed or performed in any format or medium,</li>
                                    <li>for personal research or study, educational, or not-for-profit purposes without prior permission or charge.</li>
                        </ul>
            </li>
            <#else>
            <li>If not tagged differently, copies of data can be:
                        <ul>
                                    <li>reproduced, displayed or performed, given to third parties and preserved in any format, medium or in a database</li>
                                    <li>for personal research or study, educational, <#if commercial_item_use_allowed>not-for-profit, or commercial<#else>or not-for-profit</#if> purposes without prior permission or charge provided:
                                                <ul>
                                                            <li>the authors, title and full bibliographic details are given</li>
                                                            <li>a PID and/or URL in the form of hyperlinks are given for the original metadata (landing) page</li>
                                                            <li>the original copyright statement is given</li>
                                                            <li>the content is not changed in any way</li>
                                                </ul>
                                    </li>
                        </ul>
            </li>
            </#if>
</#if>
 
<#if !commercial_item_use_allowed>
            <li>Data must not be used commercially in any format or medium without formal permission of the copyright holders.</li>
</#if>
            <li>Mention of <#if service_name_the>the </#if>${service_name} is appreciated but not mandatory.</li>
</ul>
 
<!-- CONTENT POLICY -->
 
<h2>Content Policy</h2>
 
<ul>
            <li><#if service_name_the>The </#if>${service_name} is a<#if repo_type=="institutional" || repo_type=="aggregating">n</#if> ${repo_type} repository.</li>
            <#if repo_type=="subject-based">
            <li><#if service_name_the>The </#if>${service_name} is a subject-based repository, covering these specialties:
                        <ul>
                        <#list repo_subjects as element>
                                    <li>${element}</li>
                        </#list>
                        </ul>
            </li>
            </#if>
 
            <#if item_types_overally=="all">
            <li><#if service_name_the>The </#if>${service_name} holds all types of materials.</li>
            <#else>
            <li><#if service_name_the>The </#if>${service_name} includes the following item types:
                        <ul>
                        <#list item_types as element>
                                    <li>${element}</li>
                        </#list>
                        </ul>
            </li>
            </#if>
 
            <#if opendoar_minimum><!-- Minimum: require tagging with publication status -->
            <li>Items are individually tagged with their type, date, version.</li>
            <#else><!-- Optimum: also require tagging with version, type, date, peer-review status and publication status -->
            <li>Items are individually tagged with their type, date, version, peer-review status and publication status.</li>
            </#if>
 
            <#if item_types_overally=="educational materials">
            <li><#if service_name_the>The </#if>${service_name} is restricted to scholarly material produced by affiliates of <#if service_owner_the>the </#if>${service_owner}.</li>
            <#elseif item_types_overally=="all">
            <li><#if service_name_the>The </#if>${service_name} holds all types of materials.</li>
            <#elseif item_types_overally=="data">
            <li>Deposited items may include only data.</li>
            </#if>
 
            <li>Principal languages for content:
                        <ul>
            <#list principal_languages as element>
                                    <li>${element}</li>
            </#list>
                        </ul>
            </li>
</ul>
 
<!-- SUBMISSION POLICY -->
 
<h2>Submission Policy</h2>
 
<ul>
<#if direct_submissions_allowed>
            <li>Items may only be deposited by authorised users of <#if service_name_the>the </#if>${service_name} with appropriate credentials.</li>
            <#if allowed_depositors?size!=0>
            <li>Authorised users include:
                        <ul>
                        <#list allowed_depositors as element>
                                    <li>${element}</li>
                        </#list>
                        </ul>
            </li>
            </#if>
            <li><span class="red-strong">TODO: </span>The submission process must conform to the submission guidelines in DETAILING DOCUMENT, E.G.,
<#if manual_url!=""><a target="_blank" href="${manual_url}">User Manual (${manual_url})</a><#else>OPTIONAL LINK TO User Manual (ALSO PRINTABLE URL)</#if>
AND/OR
<#if oa_policy_url!=""><a target="_blank" href="${oa_policy_url}">Open Access/Science Policy (${oa_policy_url })</a><#else>LINK TO Open Access/Science Policy (ALSO PRINTABLE URL)</#if>
and applicable embargos</li>
            <#if available_after_embargo> 
            <li>Items can be deposited at any time, but will not be made publicly accessible before any publishers&apos; or funders&apos; embargo period has expired.</li>
            <#else>
            <li>No embargo policy defined.</li>
            </#if>
 
            <#if opendoar_minimum>
            <li>The administrator only vets items for the exclusion of spam.</li>
            <#else><!-- Optimum: admin also vets items for eligibility of authors, relevance and format -->
            <li>Deposited metadata and items are subject to verification by repository administrators</li> 
            <li>The administrators vet items for the eligibility of authors/depositors, relevance to the scope of <#if service_name_the>the </#if>${service_name}, valid layout and format, and the exclusion of spam.</li>
            </#if>
 
            <li>Depositors must own all necessary rights required to deposit the data. Data depositors are held responsible for compliance with any national or international legal regulations and publisher embargoes.</li>
 
            <li>The validity and authenticity of the content of submissions and any copyright violations are the entirely responsibility of the depositor.</li>
            <#if !opendoar_minimum>
             <li>If <#if service_name_the>the </#if>${service_name} receives proof of copyright violation, the relevant item will be removed as soon as possible.</li>
            </#if>
 
            <#if file_size_limit_mb gt 0>
            <li>Files exceeding ${file_size_limit_mb} MB in size should not be directly uploaded to the repository. For files with sizes larger than ${file_size_limit_mb} MB, a special upload permission may be sought out with repository administrators.</li>
            </#if>
 
            <li>By depositing metadata and the content into <#if service_name_the>the </#if>${service_name}, the depositors accept the terms and conditions defined in this Repository Policy.</li>
<#else><!-- No direct_submissions_allowed -->
            <li><#if service_name_the>The </#if>${service_name} is an aggregating repository. Direct submissions are not possible.</li>
</#if>
</ul>

 <!-- VERSIONING POLICY -->
 
<h2>Versioning Policy</h2>
<ul>
            <li>If necessary, an updated version may be deposited.
                        <ul>
                                    <li>The earlier version may be withdrawn from public view.</li>
<#if links_to_latest_versions>
                                    <li>There will be links between versions, with the most recent version clearly identified.</li>
</#if>
<#if links_between_versions>
                                    <li>The item&apos;s permanent URL or PID will always link to the latest version.</li>
</#if>
                        </ul>
            </li> 
</ul>
 
<!-- PRIVACY POLICY -->
 
<h2>Privacy Policy</h2>
<ul>
            <li><span class="red-strong">TODO: </span><span class=”red”>This is the minimal privacy policy, suitable for repositories that collect the minimum amount of personal data and do not share it with third parties. If your repository collects more detailed personal data, customize the text accordingly.</span></li>

<#if collected_data?size != 0>
    <li>    
        Whenever a user visits the repository, the following information is automatically collected and stored:
        <ul>
            <#list collected_data as element>
                <li>${element}</li>
            </#list>
        </ul>
    </li>
</#if>

<li>This information is only used internally for technical troubleshooting, to improve the usability of the repository, and to record aggregate statistics.
The system collects personal information submitted during the deposit process. Submitted information will be used only for the purpose for which you submitted it, and will not be shared with third parties or used for commercial purposes.</li>

<#if use_google_analytics>
    <li>Google Analytics is used to analyze the use of this repository. Google's privacy policy is available at: <a href="http://www.google.com/privacypolicy.html" target="_blank">http://www.google.com/privacypolicy.html</a>.</li>
</#if>

    <li><span class="red-strong">TODO: </span><span class=”red”>If you use third-party applications, you should list them in the privacy policy and provide links to their policies relating to personal data.</span></li>
</ul>

<!-- CURATION POLICY, in line with CTS -->
 
<h2>Curation Policy</h2>
 
<ul>
<#if empty_curation_levels>
            <li>No curation policy defined.</li>
<#else>
            <li>Initial items are retained unchanged and edits are only made on copies of originals.</li>
            <li>Annotations or edits are made if allowed by relevant licences.</li>
            <li>The provided levels of curation include:
                        <ul>
<#if curation_none>
                                    <li>Content is distributed as deposited (without any changes)</li>
</#if>
<#if curation_basic>
                                    <li>Basic metadata curation (brief checking, adding basic metadata, etc.)</li>
</#if>
<#if curation_enhanced>
                                    <li>Enhanced curation (conversion to new formats, enhancement of documentation)</li>
</#if>
<#if curation_data>
                                    <li>Data-level curation (conversion to new formats, enhancement of documentation and additional editing for accuracy)</li>
</#if>
                        </ul>
            </li>
</#if>
</ul>
 
<!-- PRESERVATION POLICY -->
 
<h2>Preservation Policy</h2>

<ul>
<#if retention_period_years &gt; 0>
            <li>Items will be retained for at least <#if retention_period_years==1>one year<#else>${retention_period_years} years</#if> from the date of depositing.</li>
<#else>
            <li>Items will be retained indefinitely.</li>
</#if>

<#if regular_backups>
    <li><#if service_name_the>The </#if>${service_name} regularly backs up its files according to current best practice.</li>
</#if>

            <li><#if service_name_the>The </#if>${service_name} will try to ensure continued content readability and accessibility of the deposited items.</li>
<#if file_format_migration>
            <li>Where possible, items will be migrated to new file formats when necessary.</li>
            <li>The repository may work with external partners to convert or migrate file formats, develop and implement software emulations for old file formats, and/or record preservation metadata.</li>
            <li>Where possible, software emulations will be provided to access un-migrated formats.</li>
            <li>It may not be possible to guarantee the readability of some less common file formats over time.</li>
            <li>The original bitstream  is retained for all items, in addition to any upgraded formats.</li> 
</#if>
<#if records_withdrawal>
            <li>Items may not normally be removed from the repository.</li>
            <li>Acceptable reasons for withdrawal include:
                        <ul>
                                    <li>Publishers' policies and requirements</li>
                                    <li>Proven copyright violation or plagiarism</li>
                                    <li>Legal requirements and proven violations</li>
                                    <li>National security</li>
                                    <li>Falsified research</li>
                        </ul>
            </li>
            <li>Withdrawn items remain available for audit purposes to administrators only.</li>
            <li>Data may be removed at the request of the author/copyright holder, but metadata with explanatory notes about interventions must remain.</li>
            <li>Withdrawn items are not deleted per se, but are removed from public view.</li>
 
            <#if links_to_withdrawn_items>
            <li>Withdrawn items&apos; identifiers/URLs are retained indefinitely.</li>
            <li>URLs will continue to point to &apos;tombstone&apos; citations, to avoid broken links and to retain item histories, with:
                        <ul>
                                    <li>A link to a replacement version, where available</li>
                                    <li>A note explaining the reasons for withdrawal</li>
                        </ul>
            </li>
            <#else>
            <li>Identifiers/URLs of withdrawn items will be retained transiently. <#if service_name_the>The </#if>${service_name} may reveal a deleted status for records upon harvesting requests.</li>
            </#if>
            <li>The metadata of withdrawn items will not be searchable.</li>
            <li>Changes to deposited items are not permitted.<!-- Sometimes, updates are allowed, see below. --></li>
</#if>
<#if opendoar_optimum>
            <li>Errata and corrigenda lists may be included with the original record if required.</li>
</#if>
<#if committed_to_preserve_repository_database>
            <li>In the event of <#if service_name_the>the </#if>${service_name} being closed down, <#if service_owner_the>the </#if>${service_owner} will endeavour to transfer the database to another archive</li>
<#else>
            <li>No preservation or closure/succession policy is defined.</li>
</#if>
</ul>
 
<!-- HARVESTING POLICY -->
 
<h2>Harvesting Policy</h2>

<#if metadata_policy_flavour=="cc0">
<li>The metadata of the items deposited in <#if service_name_the>the </#if>${service_name} are made available to harvesters and aggregators under the Creative Commons <a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/">CC0 Public Domain Dedication option (“no rights reserved", https://creativecommons.org/publicdomain/zero/1.0/)</a></li>
<#elseif metadata_policy_flavour=="cc-by">
<li>Anyone can access, copy, share, modify, harvest and re-use the metadata for any purpose, free of charge and without requiring permission from <#if service_owner_the>the </#if>${service_owner}, as long as the original source of metadata of is cited</li>
<#else><!-- descriptive (OpenDOAR) -->
            <#if commercial_metadata_use_allowed><!-- Minimum -->
            <li>The metadata may be re-used by harvesters and aggregators without prior permission for not-for-profit purposes provided:</li>
            <#else><!-- Optimum: commercial metadata use -->
            <li>The metadata may be re-used by harvesters and aggregators without prior permission for not-for-profit purposes and re-sold commercially provided:
            </#if>
                        <ul>
                                    <li>The OAI Identifier or a link to the original metadata record is given</li>
                        <#if mention_repository_owner>
                                    <li><#if service_owner_the>The </#if>${service_owner} is mentioned</li>
                        </#if>
                        </ul>
            </li>
</#if>

<#if full_harvesting_robots>
            <li>Data can be harvested by robots and aggregators for full-text indexing or analysis.</li>
<#else>
            <li>Data must not be harvested by robots and aggregators except transiently for full-text indexing or citation analysis.</li>
</#if>

<!-- POLICY REVISIONS -->
 
<h2>Policy Revisions</h2>
This policy is subject to revision every ${policy_renewal_interval_years} years or whenever deemed necessary by changed circumstances or changes in best practice.
 
<!-- ABOUT CREATIVE COMMONS LICENCES -->
 
<#if include_cc_explainer>
 
<h2 id="AboutCC">About Creative Commons Licences</h2>
 
<p>If deposited under <a target="_blank" href="https://creativecommons.org/licenses/by/4.0/">Attribution CC BY</a> licence, data and metadata can be:</p>
<ul>
            <li>Accessed free of charge</li>
            <li>Shared: copied, reproduced, displayed, performed, given to third parties, redistributed and stored in a database in any medium or format</li>
            <li>Adapted: remixed, transformed, and built upon for any purpose, even commercial</li>
</ul>
<p>Unless <a target="_blank" href="https://creativecommons.org/publicdomain/zero/1.0/">CC0 Public Domain Dedication option</a> is declared, the user must provide the attribution of the original work by giving appropriate credit, providing a link to its licence and indicating if changes were made. The credit is given by providing the name the creator, copyright notice, licence notice and disclaimer notice, as well as a reference to the source<#if includes_publications>, which in the case of publications include title and full bibliographic details</#if>. The user may not imply or suggest the licensor’s endorsement of the user or their work.</p>
 
<#if mention_cc_by_sa || mention_cc_by_nc_sa>
            <p>With Share-alike condition (if <a target="_blank" href="https://creativecommons.org/licenses/by-sa/4.0/">CC BY-SA</a> or <a target="_blank" href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA</a> licence is declared), any adaptation or derived contributions must be distributed under the same licence as the original or under a compatible one.</p>
</#if>
 
<#if mention_cc_by_nc || mention_cc_by_nc_sa>
            <p>With No Commercial Use condition (if <a target="_blank" href="https://creativecommons.org/licenses/by-nc/4.0/">CC BY-NC</a> or <a target="_blank" href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA</a> licence is declared), only non-commercial uses of the item are allowed (for example, personal research or study, education, and other not-for-profit uses) unless a permission for commercial use (that is, primarily intended for commercial advantage or monetary compensation) is explicitly provided by the authors to a licensee.</p>
</#if>
            <p>With any of Creative Commons licences, the licensor may not restrict others from doing anything the licence allows, or revoke the granted freedoms as long as the licence terms are followed. Also, the users do not have to comply with the licence for the elements of the work that are in the public domain or for uses allowed by an applicable exception or limitation.</p>
            <p>Creative Commons licences do not give any warranties related to other rights such as publicity, privacy, or moral rights that may otherwise limit the use of the licensed works.</p>
            <p>CC0 may be required by the author or funder or be appropriate for databases, datasets, well-known facts, old works, and cultural heritage that are without ownership or copyright or in the public domain in all jurisdictions.</p>
            <p>For more information about Creative Commons licences, please visit the <a target="_blank" href="http://creativecommons.org/choose/">CC Licence Chooser</a>.</p>
</#if>
 
<!-- GENERATED FOOTER -->
 
<div class="generated_footer">R
            <p><i>Created by <a href="${repol_url}" target="_blank">RePol (${repol_url})</a> version ${repol_version}, <a href="https://github.com/RCUB-Official/RePol" target="_blank">source code at GitHub</a> under <a href="https://spdx.org/licenses/EUPL-1.2.html" target="_blank">EUPL-1.2</a>, on ${current_time?string("yyyy-MM-dd HH:mm zzz")}</i></p>
</div>

            </body>
</html>

