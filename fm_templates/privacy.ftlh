${embedded_data?no_esc}
<html>
    <head>
            <title>Privacy Policy - ${service}</title>
        <meta charset="utf-8"/>
            <style>
                    td {
                            border: 1px solid #eee;
                            padding:10px;
                            padding-right:25px;
                    }
                    .red, red-strong {
                            color:red;
                    }
                    .red-strong {
                            font-weight:bold;
                    }
            </style>
    </head>
                <#assign hosting_cuntry = "<TODO-PARAMETRIZED COUNTRY>">
    <body>
<h1>Privacy Policy - ${service}</h1>
<div class="generated_header">
    <p>Date (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}</p>
</div>
<!-- CONTENT -->
<p>This is the Privacy Policy for ${service}, owned and maintained by ${service_owner}.
${service_owner} is dedicated to safeguarding the personal data subject to processing, as well as to developing and applying data protection solutions that are effective, fit for purpose and demonstrate an understanding of, and appreciation for the <#if gdpr>General Data Protection Regulation 679/2016 (GDPR) as well as </#if>applicable national data protection legislation.</p>
<p>We are committed to ensuring ongoing and continued compliance with data protection legislation and guidance provided by the supervisor of our general work and practices and all of our service offerings, to protecting any personal data, limiting any collection to what is strictly necessary by <#list legal_basis as element>${element}<#if element?has_next><#if element?index&lt;legal_basis?size-2>, <#else> and </#if></#if></#list> and complying with the <#list compliance_includes as element>${element}<#if element?has_next><#if element?index&lt;compliance_includes?size-2>, <#else> and </#if></#if></#list>.</p>
<p>This Privacy Policy concerns the processing of your personal data for which ${service_owner} is the Data Controller or Data Processor, in other words, ${service_owner} collects personal data directly or indirectly from you and decides the personal data needed to provide you with a service, the purposes for which your personal data is being collected, how it is stored, shared and for how long will be retained.</p>
<p>Considering the significance of this responsibility, ${service_owner} works daily to put in place adequate measures and controls to protect your personal data and ensure that you can exercise your rights at any time.</p>
<#if collection_methods?size!=0>
<p>Personal identification information from the users is collected through:</p>
<ul>
    <#list collection_methods as element>
    <li>${element}</li>
    </#list>
</ul>
</#if>

<#if collected_data?size != 0>
<h2>Types of data collected and processed</h2>
<p>${service_owner} processes:</p>
<ul>
    <#list collected_data as element>
    <li>${element}</li>
    </#list>
</ul>
</#if>

<h2>Methods of data collection</h2>
<p>The following tools are used to collect user and usage data:</p>
<#if cm_main_service>
<h3>Main service</h3>
<p>Whenever a user visits the service, the following information is automatically collected and stored:</p>
            <ul>
                <#list collected_data as element>
                    <li>${element}</li>
                </#list>
            </ul>
</#if>

<#if collected_data_from_idp?size != 0>
<h3>Data from identity providers</h3>
<p>Whenever a user logs into the service using an external identity provider, the following information is automatically collected and processed:</p>
        <ul>
            <#list collected_data_from_idp as element>
                <li>${element}</li>
            </#list>
        </ul>
<!--
Supplementary technical clarification: The most frequently used corresponding SAML attributes are (based on eduGAIN service stats):
- eduPersonPrincipalName - Scoped identifier in the form "user@scope"
- mail - Preferred address for the "to:" field of email
- givenName - First name, given names of a person
- sn, surname - Surname, family names of a person
- eduPersonTargetedID - Persistent, non-reassigned, opaque identifier for a principal
- cn, commonName - Name of an object; typically the person's full name
- eduPersonScopedAffiliation - Person's affiliation within a particular security domain: student, faculty, staff, alum, etc.
- displayName - Common name (preferred name of a person to be used when displaying entries)
- eduPersonAffiliation - Type of affiliation: student, faculty, staff, alum, etc.
- eduPersonEntitlement - Indication of rights to specific resources
o - Organisation name
- uid -  Computer system login name pr person’s identifier with organisation, typically the LDAP directory username
- eduPersonUniqueId - Long-lived, non re-assignable, omnidirectional identifier
- email - Institutional email
- schacHomeOrganization - Person's home organisation using the domain name of the organization
- eduPersonPrimaryAffiliation - Person's primary relationship to the institution: student, faculty, staff, alum, etc.
- preferredLanguage - Preferred written or spoken language
- eduPersonOrgUnitDN - Distinguished name of the directory entries representing the person's organisational unit
- telephoneNumber - Office/campus phone number
- ou - Organisational unit
eduPersonOrgDN - Distinguished name of the directory entry representing the institution
- eduPersonAssurance - Compliance with specific standards for identity assurance
- eduPersonOrcid - Researcher's ORCID iD, a persistent digital identifier
-->
<p>This data is provided by the institution which has created and which maintains your account. We assume that by logging into the service you consent to the release of your institutional data to our service.</p>
</#if>

<#if web_analytics_tools?size!=0>
<h3>Web analytics</h3>
<p>Insights from web analytics tools help us improve content and user experience.</p>

<#if use_matomo>
<p>Matomo is maintained by ${service_owner}. Your IP address is anonymised, and your full IP address is not stored. Usage information is aggregated and is neither identifiable on an individual basis, nor is not sent to third parties</p>
<ul>
    <li>This information is only used internally for technical troubleshooting, to improve the usability of the repository, and to record aggregate statistics.</li>
    <li>The system collects personal information submitted during the deposit process. Submitted information will be used only for the purpose for which you submitted it, and will not be shared with third parties or used for commercial purposes.</li>
</ul>
</#if>

<#if use_google_analytics>
<p>Google Analytics is used to analyse the use of this service. Google's privacy policy is available at: <a href="http://www.google.com/privacypolicy.html" target="_blank">http://www.google.com/privacypolicy.html</a>.</p>
<ul>
                <li>We use Google Analytics to track how often people access or read our content. We use this information in the aggregate form to understand what content our members find useful or interesting, so we can produce the most valuable content to meet your needs.</li>
                <li>We use Google Analytics to track the number of visitors to our site but do not collect any personal information or store IP addresses locally on our site. This is only used to monitor the number of hits on our pages and the coarse location; therefore we cannot track or trace individual users or their physical addresses. If you would like to opt-out, please use the Google Analytics Opt-out Browser Add-on https://tools.google.com/dlpage/gaoptout.</li>
</ul>
</#if>

                <#if web_analytics_other>
<p><span class="red-strong">TODO: </span>DESCRIBE WEB ANALYTICS SOFTWARE USED TO ANALYSE THE USE OF THIS SERVICE. The relevant privacy policy is available at LINK.</p>
                </#if>
</#if>

<#if cookies!='not used'>
<h3>Cookies</h3>
<p>We use cookies (small text files written by your browser to your device) to track returning visits and navigation through the website. Cookies are <#if cookies=='required'>required<#else>not required</#if> to use the website.</p>
<p>By using our service’s website, you agree that we can place these types of cookies on your device. However, you can block these cookies using your browser settings<#if cookie_policy_url!=""> (there are instructions in the Cookie Policy on how to do this)</#if>.</p>
<p>${service_owner} recommends you to accept and use cookies.</p>
<#if cookie_policy_url!="">
<p>A full Cookie Policy can be seen by clicking <a target="_blank" href="${cookie_policy_url}">${service} Cookie Policy (${cookie_policy_url})</a>, which will provide further details on:</p>
<ul>
                <li>how we use cookies</li>
                <li>how to enable/disable cookies</li>
                <li>how to change cookie settings</li>
                <li>the cookies used on the websites</li>
                    <#if third_party_cookies>
                <li>third-party cookies</li>
                </#if>
</ul>
                </#if>
</#if>

<#if collection_methods?size!=0>
            <#if cm_registration>
<h3>Registration</h3>
<p>When you submit a registration through the registration form, ${service_owner} stores your name, email address, institutional affiliation, position and <span class="red-strong">TODO</span>. This data is stored in <span class="red-strong">TODO</span>. The data processor for this service is <span class="red-strong">TODO</span>.</p>
            </#if>

            <#if cm_feedback>
<h3>Feedback form</h3>
                <p>When using the feedback form, ${service_owner} collects the information you enter, including your email address and name, if you provide it. The legal basis is the legitimate interest to provide user support and the user consent implied by submitting the entered data. The data processor for this service is <span class="red-strong">TODO</span>.</p>
            </#if>

            <#if cm_newsletter>
<h3>Newsletter</h3>
<p>If you sign up for the newsletter, you give consent to record and use your email address to send you regular newsletters. The data processor for this service is <span class="red-strong">TODO</span>. You may withdraw your consent and unsubscribe at any time by clicking the unsubscribe link in any received newsletter.</p>
            </#if>
            <#if cm_subdomains>
<h3>Services in subdomains</h3>
            </#if>

            <#if cm_third_party_services>
<h3>Third-party services</h3>
<p><span class="red-strong">TODO: </span><p>DESCRIBE THE TYPE OF INFORMATION COLLECTED AND NAME THE RESPONSIBLE DATA PROCESSOR.</p>
            </#if>
            <#if cm_other>
<h3>Other</h3>
<p><span class="red-strong">TODO: </span>DESCRIBE THE METHOD OF DATA COLLECTION, TYPE OF INFORMATION COLLECTED AND NAME THE RESPONSIBLE DATA PROCESSOR.</p>
            </#if>
</#if>

<#if processor_country!="">
    <#assign country=processor_country>
<#else>
    <#assign country=supervisor_country>
</#if>

<h2>Data storage and retention periods</h2>
<p>All data is stored in ${country}. The website and services are running on a server in ${country}. Data stored by third parties is covered by appropriate data processing agreements.</p>


<p>Your personal information is stored for the duration of your use of the service provided<#if duration_of_storage!=0> and additional ${duration_of_storage} months after you cease using our services</#if>. We keep this data to deal with any queries that may arise following your use of our service.</p>
<p>Personal data is only stored as long as it is necessary to provide a requested service dependent on this data.</p>
        <#if duration_of_storage_ip!=0>
<p>Identifiable IP addresses are stored for ${duration_of_storage_ip} months.</p>
        <#else>
<p>Identifiable IP addresses are not stored.</p>
        </#if>
<p>In certain circumstances, data may be retained longer for archiving in the public interest, scientific or historical research purposes, and in the context of long-term preservation.</p>

<h2>Security of your personal data</h2>
<p>${service_owner} takes the confidentiality, integrity and availability of your personal data very seriously. We take appropriate security precautions to protect your personal data from loss, misuse or unauthorised access, disclosure, alteration and destruction. When you access ${service}, ${service_owner} will provide adequate security controls to keep your personal data safe in accordance with the classification of the personal data we have collected from you.</p>
<p>Although we endeavour to ensure your personal data remains secure, there is no absolute guarantee of security when using services online. While we strive to protect your personal data, you acknowledge that:</p>
<ul>
            <li>There are security and privacy limitations on the Internet which are beyond our control.</li>
            <li>The confidentiality, integrity and availability of any and all information exchanged using these services cannot be 100% guaranteed.</li>
            <li>We cannot be held accountable for activities that result from your own neglect to safeguard the security of your login credentials and equipment that results in a loss of your personal data.</li>
</ul>
<p>If you feel this is not sufficient, then please do not provide any personal data.</p>
        

<h3>When and how we share information with others</h3>
<#if data_sharing>
<p>We provide and support some of our services through contractual arrangements with service owners and other third parties. ${service_owner} and our service partners use your personal data to operate our website(s) and provide services and events to you.</p>
<p>The personal information ${service_owner} collects from you is stored in one or more databases hosted by third parties <#if gdpr>that may be located within the EU or outside the EU</#if>. These third parties do not use or have the authority to access your personal data for any reason other than cloud storage and retrieval.</p>
</#if>
<p>We will disclose personal data in the following circumstances:</p>
<ul>
            <li>if we are required or permitted to do so by law, regulatory or other legal processes</li>
            <li>to protect our rights, reputation, property or the safety of us and others</li>
            <li>to defend or enforce our rights or your obligations</li>
</ul>
<p>We do not sell personal information to anyone or share it with third parties who are facilitating the delivery of other services.</p>
<#if gdpr>
<h3>Transferring personal data from the EU</h3>
<p>${service} is located in ${supervisor_country}. Personal data we collect about you will generally be processed in its locations. ${service_owner} endeavours to apply suitable safeguards to protect the privacy and security of your personal data and to use it only within the authorisation you have given and the practices described in this Privacy Policy.</p>
                <#if possible_transfer_outside_eu>
<p>In some cases, ${service_owner} may transfer your personal data to countries outside the European Economic Area (EEA), for example, if we use a third party for processing services or storing data, or for obtaining feedback on one of the services or events we provide. If we transfer personal data to third countries, we will ensure that such transfers are compliant with our obligations under relevant data protection legislation and that appropriate technical and operational controls are in place to keep your personal data secure.</p>
<p>If personal data is transferred from the EU to the USA, the EU-US Privacy Shield will be used. This framework was developed to enable companies to comply with data protection requirements when transferring personal data from the EU to the USA.</p>
                </#if>
<p>${service_owner} also minimises the risk to your rights and freedoms by not collecting or storing any sensitive information about you without your explicit consent.</p>

<h2>Your rights under the GDPR / Data subject rights</h2>
<p>The General Data Protection Regulation (GDPR) establishes several rights for individuals concerning their personal data, such as:</p>
<ul>
            <li>To know about what personal data is kept.</li>
            <li>To request access to your data.</li>
            <li>To ask us to rectify your personal data.</li>
            <li>To ask us to erase your personal information.</li>
            <li>To object to your data being processed by us.</li>
</ul>
<p>You also have the right to inquire what personal data we hold about you, and to present a complaint to the relevant Supervisory Authority about our personal data processing activities if you feel your personal data is not being managed as described here.</p>
<p>Please note that submitting a Data Subject Access Request might be connected with providing information confirming your identity and the legal basis for this processing is compliance with a legal obligation (ensuring data subject access request).</p>
     
<p>The erasure of personal information or termination of your data processing by us may limit service, access to it or the results or outcomes of previous use of the service, especially if these results are inherently related to the presence of personal information.</p>
<h3>Access to personal data</h3>
<p>You may submit a Subject Access Request to ${service_owner}-. The response will include a description of your data, if any, any recipients of it, as well as a copy of your data. Any request regarding Data Subject Rights will be responded to within <span class="red-strong">TODO</span>. </p>
<p>Any request regarding Data Subject Rights will be responded to within <span class="red-strong">TODO</span>. </p>
<h3>Right to rectification and erasure</h3>
<p>You have the right to correct any inaccurate personal data held by ${service_owner}. You also have the right to request the erasure of any data that is no longer necessary for the purpose for which it was collected.</p>
<h3>Right to restriction of processing and withdrawal of consent</h3>
<p>You have the right to restrict the processing of your personal data or object to the processing of your data. You also have the right to withdraw previously given consent to process personal data.</p>
<h3>Rights to data portability</h3>
<p>You have the right to receive your personal data that ${service_owner} holds by consent in a structured, commonly used and machine-readable format. You also have the right to transmit that data to another controller without hindrance from the ${service_owner}.</p>
    <#if automated_processing>
<h2>Automated processing</h2>
<p>Our services use automated processing to detect and respond to problems more efficiently. If you feel any decision made by any automated process has affected you in a way that is not acceptable, you can contact the Data Protection Officer to ask us to review the results manually.</p>
    </#if>
</#if><!--- GDPR --->
<#if national_legislation>
<h2>National legislation</h2>
<p><span class="red-strong">TODO: </span>NAME OF RELEVANT REGULATION establishes several rights for individuals concerning their personal data:</p>
        <ul>
                <li><span class="red-strong">TODO: </span>LIST RIGHTS HERE</li>
        </ul>
        <p><span class="red-strong">TODO: </span> MORE DETAILED DESCRIPTION OF THE RIGHTS AND RESPONSIBILITIES.</p>
</#if>
<h2>Contacts</h2>
<h3>Data Protection Officer / Contact for questions regarding personal data and privacy</h3>
<p>${service_owner}, as the owner and data controller of ${service}, has appointed a Data Protection Officer (DPO), for you to contact if you have any questions or concerns about the way ${service_owner} is processing your personal data. Together, the DPO and ${service_url} service team are working to provide you with accurate information and implement measures to protect your privacy.
<p>For questions, comments or complaints regarding personal data and privacy, this Privacy Policy and how ${service_owner} processes your personal, and to exercise data subject rights, please contact the Data Protection Officer:</p>
<table>
                <tr><td>Name</td><td>${dpo_name}</td></tr>
                <tr><td>Organisation</td><td>${dpo_organization}</td></tr>
                <tr><td>Address</td><td>${dpo_address}</td></tr>
                <tr><td>Country</td><td>${dpo_country}</td></tr>
                <tr><td>Email</td><td>${dpo_email}</td></tr>
                <tr><td>Telephone</td><td>${dpo_telephone}</td></tr>
</table>



<h3>Supervisory authority</h3>
<p>Should you wish to report a complaint about ${service_owner}’s data processing that has not been addressed adequately, you may contact:<</p>
<table>
                <tr><td>Name</td><td>${supervisor_name}</td></tr>
                <tr><td>Organisation</td><td>${supervisor_organization}</td></tr>
                <tr><td>Address</td><td>${supervisor_address}</td></tr>
                <tr><td>Country</td><td>${supervisor_country}</td></tr>
                <tr><td>Email</td><td>${supervisor_email}</td></tr>
                <tr><td>Telephone</td><td>${supervisor_telephone}</td></tr>
</table>
<h2>Changes and updates to the Privacy Policy</h2>
<p>This service may change from time to time and this Privacy Policy will be updated appropriately. We reserve the right to amend the Privacy Policy at any time – in such case, an updated version of the Privacy Policy will be posted at this location and we encourage you to check it from time to time. <#if policy_update_notifications>We may email periodic notifications of our notices and terms that are currently in effect and any changes that may have been made to them.</#if></p>
<p>${service_owner} keeps this Privacy Policy under regular review; the last update was on (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}</p>

<!-- GENERATED FOOTER -->
         <div class="generated_footer">
            <p><i>Created by <a href="${repol_url}" target="_blank">RePol (${repol_url})</a> version ${repol_version}, <a href="https://github.com/RestlessDevil/RePol" target="_blank">source code at GitHub</a> under <a href="https://spdx.org/licenses/EUPL-1.2.html" target="_blank">EUPL-1.2</a>, on ${current_time?string("yyyy-MM-dd HH:mm zzz")}</i></p>
        </div>
    </body>
</html>
