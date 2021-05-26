${embedded_data?no_esc}
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Privacy Policy - ${service}</title>
        <style>
            td {
                border: 1px solid #eee;
                padding:10px;
                padding-right:25px;
            }

            .red, .red-strong {
                color:red;
            }

            .red-strong {
                font-weight:bold;
            }
        </style>
    </head>

    <!-- PLACEHOLDER VARIABLES -->
    <#assign reminders = true>
    <#assign automated_processing = true>
    <#assign data_sharing = true>
    <#assign possible_transfer_outside_eu = true>
    <#assign stored_as_necessary = true>

    <#assign cm_other = true>

    <body>

        <h1>Privacy Policy - ${service}</h1>
 
        <div class="generated_header">
            <p>Date (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}</p>
        </div>
 
        <!-- CONTENT -->
        <p>This is the Privacy Policy for ${service}, owned and maintained by ${service_owner}.
${service_owner} is dedicated to safeguarding the personal data that we process, as well as to developing and applying data protection solutions that are effective, fit for purpose and demonstrate an understanding of, and appreciation for the <#if gdpr>General Data Protection Regulation 679/2016 (GDPR) as well as </#if>applicable national data protection legislation.</p>
        <p>We are committed to ensuring ongoing and continued compliance with data protection legislation and guidance provided by the supervisory of our general work and practices and all of our service offerings,<!--SPOJITI SA PRETHODNIM?...--> to protecting any personal data, limiting any collection to what is strictly necessary by <#list legal_basis as element>${element}<#if element?has_next><#if element?index&lt;legal_basis?size-2>, <#else> and </#if></#if></#list> and complying with the <#list compliance as element>${element}<#if element?has_next><#if element?index&lt;compliance?size-2>, <#else> and </#if></#if></#list>.</p>
        <p>This Privacy Policy concerns the processing of your personal data for which ${service_owner} is the Data Controller or Data Processor, in other words, ${service_owner} collects personal data directly or indirectly from you and decides the personal data needed to provide you with a service, the purposes for <b>which</b> your personal data is being collected, <b>how</b> it is stored, <b>shared</b> and <b>for how long</b> will be retained.</p>
        <p>Considering the significance of this responsibility, ${service_owner} works on daily basis to put in place adequate measures and controls to protect your personal data, improve them and ensure that you can exercise your rights at any time.</p>

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
        <p>The following web analytics <!-- - zašto web analytics?/-->tools are used to D/track web traffic patterns/:</p>
        <#if collected_data?size != 0>
            <!-- /Use of/By/ service -->    
            <p>Whenever a user visits the service, the following information is automatically collected and stored:</p>
            <ul>
                <#list collected_data as element>
                    <li>${element}</li>
                </#list>
            </ul>
        </#if>

        <#if collected_data_from_idp?size != 0>
        <h3>From identity provider</h3>
        <p>Whenever a user logs into  the service using an external identity provider, the following information is automatically collected and stored<!--Šta ako je samo proveravana, a nije čuvana (pripadnost, prava, afilijacija)?-->:</p>
        <ul>
            <#list collected_data_from_idp as element>
                <li>${element}</li>
            </#list>
        </ul>

<!--Supplementary technical clarification: The most frequent corresponding SAML attribute names are (based on eduGAIN service stats):
eduPersonPrincipalName - Scoped identifier in the form "user@scope"
mail - Preferred address for the "to:" field of email
givenName - First name, given names of a person
sn, surname - Surname, family names of a person
eduPersonTargetedID - Persistent, non-reassigned, opaque identifier for a principal
cn, commonName - Name of an object; typically the person's full name
eduPersonScopedAffiliation - Person's affiliation within a particular security domain: student, faculty, staff, alum, etc.
displayName - Common name (preferred name of a person to be used when displaying entries)
eduPersonAffiliation - Type of affiliation: student, faculty, staff, alum, etc.
eduPersonEntitlement - Indication of rights to specific resources
o - Organisation name
uid -  Computer system login name pr person’s identifier with organisation, typically the LDAP directory username
eduPersonUniqueId - Long-lived, non re-assignable, omnidirectional identifier
email - Institutional email
schacHomeOrganization - Person's home organisation using the domain name of the organization
eduPersonPrimaryAffiliation - Person's primary relationship to the institution: student, faculty, staff, alum, etc.
preferredLanguage - Preferred written or spoken language
eduPersonOrgUnitDN - Distinguished name of the directory entries representing the person's organisational unit
telephoneNumber - Office/campus phone number
ou - Organisational unit
eduPersonOrgDN - Distinguished name of the directory entry representing the institution
eduPersonAssurance - Compliance with specific standards for identity assurance
eduPersonOrcid - Researcher's ORCID iD, a persistent digital identifier
-->
            <p>This data is provided by the institution which created and maintains your account. We assume that by logging into the service you consent with release of your institutional data to our service.</p>
        </#if>

        <#if web_analytics_tools?size!=0>
            <h2>Web analytics</h2>

            <#if use_matomo>
                <p>Matomo is maintained by ${service_owner}. Your IP address is anonymised, and your full IP address is not stored. Usage information is aggregated and not identifiable on an individual basis and is not sent to third parties</p>
                <ul>
                    <li>This information is only used internally for technical troubleshooting, to improve the usability of the repository, and to record aggregate statistics.</li>
                    <li>The system collects personal information submitted during the deposit process. Submitted information will be used only for the purpose for which you submitted it, and will not be shared with third parties or used for commercial purposes.</li>
                </ul>
            </#if>

            <#if use_google_analytics>
                <p>Google Analytics is used to analyze the use of this service. Google's privacy policy is available at: <a href="http://www.google.com/privacypolicy.html" target="_blank">http://www.google.com/privacypolicy.html</a>.</p>
                <ul>
                    <li>We use Google Analytics to track how often people gain access to or read our content. We use this information in the aggregate to understand what content our members find useful or interesting, so we can produce the most valuable content to meet your needs.</li>
                    <li>We use Google Analytics to track the number of visitors to our site but do not collect any personal information or store IP addresses locally on our site. This is only used to monitor the number of hits on our pages and the location at the country level, therefore we cannot track or trace individual users or their physical addresses. If you would like to opt-out, please use the LINK!!! Google Analytics Opt-out Browser Add-On https://tools.google.com/dlpage/gaoptout.</li>
                </ul>
            </#if>

            <#if web_analytics_other>
                <p>Other: XXX is used to analyze the use of this service. /The/ relevant privacy policy is available at LINK (TO DO).</p>
            </#if>
        </#if>

        <#if use_of_cookies!='not used'>
            <h2>Cookies</h2>
            <p>We use cookies (small text files written by your browser to your device) to track returning visits and navigation paths through the website. Cookies are <#if use_of_cookies=='required'> required<#else>not required</#if> /to use/for using of/ the website.</p>
            <p>By using our service’s website, you agree that we can place these types of cookies on your device, however, you can block these cookies using your browser settings (there are instructions below on how to do this).</p>
            <p>${service_owner} recommends you to accept and use cookies.</p>
            <!--Potencijalno moze jos-->
            <p>A full Cookie Policy can be seen by clicking TODO: LINK which will provide further details on:</p>
            <ul>
                <li>How we use cookies;</li>
                <li>How to enable/disable cookies;</li>
                <li>How to change cookie settings;</li>
                <li>The cookies used on the websites;</li>
                <li>Third-party cookies.</li>
                <#if third_party_cookies>
                    <li>Third-party cookies include: listed under Third-party cookies:</li>
                </#if>
            </ul>
        </#if>
        <#if collection_methods?size!=0>
            <#if cm_registration>
                <h3>Registration</h3>
                <p>When you submit a registration through the registration form, ${service_owner} stores your name, email address, institutional affiliation, position and TO DO. This data is stored in TO DO. The data processor for this service is TO DO.</p>
            </#if>

            <#if cm_feedback>
                <h3>Feedback form</h3>
                <p>When using the feedback form, ${service_owner} collects the information you enter, including your email address and name, if you provide it. The legal basis is the legitimate interest to provide user support and the user consent implied by submitting the entered data. The data processor for this service is TO DO.</p>
            </#if>

            <#if cm_newsletter>
                <h3>Newsletter</h3>
                <p>If you sign up for the newsletter, you give consent to record and use your email address in order to send you regular newsletters. The data processor for this service is TO DO. You may withdraw your consent and unsubscribe at any time by clicking the unsubscribe link in any received newsletter.</p>
            </#if>

            <#if cm_subdomains>
                <h3>Services in subdomains</h3>
            </#if>

            <#if cm_third_party>
                <h3>Third-party services</h3>
            </#if>

            <#if cm_other>
                <h3>Other</h3>
                <p>Other: TO DO: describe the type of information collected and name the responsible data processor.</p>
            </#if>
        </#if>

        <h2>Data storage and retention periods</h2>
        <p>All data is stored in value under Data is stored in. The website and services are running on a server in value under Server is located in. Data stored by third parties is covered by appropriate data processing agreements.</p>
        <p><!-- Personal data is stored for ${duration_of_storage} months. //ALT: -->Your personal information is stored for the duration of your use of the service provided<#if duration_of_storage!=0> and an additional ${duration_of_storage} months after you cease using our services. </#if>We keep this data in order to deal with any queries that may arise following your use of our service <#if stored_as_necessary> and as long as necessary</#if>.</p>
        <p>Personal data is only stored as long as it is necessary to provide a requested service dependent on this data.</p>
        <#if duration_of_storage_ip!=0>
            <p>Identifiable IP addresses are stored for ${duration_of_storage_ip} months.</p>
        <#else>
            <p>Identifiable IP addresses are not stored.</p>
        </#if>
        <!-- Treba li  ovde opcija? -->
        <p>In certain circumstances, data may be retained longer for archiving in the public interest, scientific or historic/al???/ research purposes.</p>


        <h2>Security of your personal data</h2>
        <p>${service_owner} takes the confidentiality, integrity and availability of your personal data very seriously. We take appropriate security precautions to protect your personal data from loss, misuse or unauthorised access, disclosure, alteration and destruction. When you access ${service}, ${service_owner} will provide adequate security controls to keep your personal data safe in accordance with the classification of the personal data we have collected from you.</p>
        <p>Although we endeavour to ensure your personal data remains secure, there is no absolute guarantee of security when using services online. While we strive to protect your personal data, you acknowledge that:</p>
        <ul>
            <li>there are security and privacy limitations on the Internet which are beyond our control;</li>
            <li>the confidentiality, integrity and availability of any and all information exchanged using these services cannot be 100% guaranteed;</li>
            <li>we cannot be held accountable for activity that results from your own neglect to safeguard the security of your login credentials and equipment that results in a loss of your personal data.</li>
        </ul>
        <p>If you feel this is not sufficient then please do not provide any personal data.</p>
        
        <#if data_sharing>
            <!-- /How data is shared/ C/možda i ovo pod GDPR/-->
            <h3>When and how we share information with others</h3>
            <p>We provide and support some of our services through contractual arrangements with service owners and other third parties. ${service_owner} and our service partners use your personal data to operate our website(s) and provide services and events to you.</p>
            <p>FOR external hosting? The personal information ${service_owner} collects from you is stored in one or more databases hosted by third parties <#if gdpr>that may be located within the EU or outside the EU</#if>. These third parties do not use or have authority to access /to/ your personal data for any reason other than cloud storage and retrieval.</p>
        </#if>

        <p>We will<#if data_sharing> also</#if> disclose personal data in the following circumstances:</p>
        <ul>
            <li>if we are required or permitted to do so by law, regulatory or other legal process/es/;</li>
            <li>to protect our rights, reputation, property or the safety of us and others;</li>
            <li>to defend or enforce our rights or your obligations.</li>
        </ul>
        <p>We do not sell personal information to anyone or share it with third parties who are facilitating the delivery of other services.</p>
        <#if gdpr>
            <h3>Transferring Personal Data from the EU</h3>
            <p>${service} is located in ${supervisor_country}. Personal Data we collect about you will generally be processed in its locations. ${service_owner} endeavours to apply suitable safeguards to protect the privacy and security of your personal data and to use it only within the authorisation you have given and the practices described in this Privacy Policy.</p>
                <#if possible_transfer_outside_eu>
                    <p>In some cases, ${service_owner} may transfer your personal data to countries outside the European Economic Area (EEA), for example, if we use a third party for processing services or storing data or for obtaining feedback on one of the services or events we provide. If we do transfer personal data to third countries, we will ensure that such transfers are compliant with our obligations under relevant data protection legislation and that appropriate technical and operational controls are in place to keep your personal data secure.
If personal data is transferred from the EU to the USA, the EU-US Privacy Shield will be used. This framework was developed to enable companies to comply with data protection requirements when transferring personal data from the EU to the USA.</p>
                </#if>
        </#if>
        <p>${service_owner} also minimises the risk to your rights and freedoms by not collecting or storing any sensitive information about you without your explicit consent.</p>


        <h2>Your rights under the GDPR/Data subject rights</h2>
        <p>The General Data Protection Regulation (GDPR) establishes /several/a number of/ rights for individuals concerning their personal data !!!PREKLAPANJE SA BULLETIMA ISPOD???, such as the right to information about what personal data is kept, to have it corrected or erased, and to protest against its processing.</p>
        <ul>
            <li>You have the following rights regarding your personal data:</li>
            <li>You have the right to request access to your data.</li>
            <li>You have the right to ask us to rectify your personal data.</li>
            <li>You have the right to ask us to erase your personal information.</li>
            <li>You have the right to object to your data being processed by us.</li>
        </ul>
        <p>You also have the right to inquire what personal data we hold about you, and to present a complaint to the relevant Supervisory Authority about our personal data processing activities if you feel your personal data is not being managed as described here.</p>
        <p>Please note that /submitting Data Subject Access Request/ might be connected with providing information confirming your identity and the legal basis for this processing is compliance with /a/ legal obligation (ensuring data subject access request).</p>
        <!--/Možda i da (pod posebnom pitalicom):
        <p>Erase of personal information or termination of your data processing by us may limit service, access of it or the results or outcomes of previous use of the service, especially if these results are inherently related to the presence of personal information./<p>-->

        <h3>Access to personal data</h3>
        <p>You may make a Subject Access Request to ${service_owner}, which will be responded to within TO DO. The response will include a description of your data, /if any, /any recipients of it, as well as a copy of your data.</p>

        <h3>Right to rectification and erasure</h3>
        <p>You have the right to correct any inaccurate personal data held by ${service_owner}. You also have the right to request /the/ erasure of any data that is no longer necessary for the purpose for which it was collected.</p>
        <h3>Right to restriction of processing and withdrawal of consent</h3>
        <p>You have the right to restrict the processing of your personal data or object to the processing of your data. You also have the right to withdraw previously given consent to process personal data.
If you wish to withdraw consent for the processing of your personal data or otherwise restrict or object to the processing of your personal data please contact.</p>
        <!-- value under Contact for questions regarding personal data and privacy. -->
        
        <h3>Rights to data portability</h3>
        <p>You have the right to receive your personal data that ${service_owner} holds by consent in a structured, commonly used and machine-readable format. You also have the right to transmit that data to another controller without hindrance from the ${service_owner}.</p>


        <#if automated_processing>
            <h2>Automated Processing</h2>
            <p>Our services use automated processing to detect and respond to problems more efficiently. If you feel any decision made by any automated process has affected you in a way that is not acceptable, you can contact the Data Protection Officer to ask us to review the results manually.</p>
            <p>If national legislation or other:
            Your rights under TO DO: name the relevant regulation(s)<br/>
                <span class="red-strong">TODO: </span>NAME OF THE RELEVANT REGULATION(S) establishes a number of rights for individuals concerning their personal data: LIST RIGHTS HERE</p>
            <p>A MORE DETAILED DESCRIPTION OF THE RIGHTS AND RESPONSIBILITIES.</p>
        </#if>

        <h2>Contacts</h2>
        <h3>Data Protection Officer / Contact for questions regarding personal data and privacy</h3>
        <p>${service_owner}, as the owner and data controller of ${service},  has appointed a Data Protection Officer (DPO), for you to contact if you have any questions or concerns about the way ${service_owner} is processing your personal data. Together, the DPO and ${service_url} service team are working to provide you /with/ accurate information and implement the measures to protect your privacy.
For questions, comments or complaints regarding personal data and privacy, this Privacy Policy and how ${service_owner} processes your personal and to exercise data subject rights, please contact the Data Protection Officer:</p>
        <table>
            <tr><td>NAME</td><td>${dpo_name}</td></tr>
            <tr><td>ORGANIZATION</td><td>${dpo_organization}</td></tr>
            <tr><td>ADDRESS</td><td>${dpo_address}</td></tr>
            <tr><td>COUNTRY</td><td>${dpo_country}</td></tr>
            <tr><td>EMAIL</td><td>${dpo_email}</td></tr>
            <tr><td>TELEPHONE</td><td>${dpo_telephone}</td></tr>
        </table>

        <h3>Supervisory authority</h3>
        <p>Should you wish to report a complaint about ${service_owner}’s data processing which has not been addressed in a satisfactory manner, you may contact:</p>
            <table>
                <tr><td>NAME</td><td>${supervisor_name}</td></tr>
                <tr><td>ORGANIZATION</td><td>${supervisor_organization}</td></tr>
                <tr><td>ADDRESS</td><td>${supervisor_address}</td></tr>
                <tr><td>COUNTRY</td><td>${supervisor_country}</td></tr>
                <tr><td>EMAIL</td><td>${supervisor_email}</td></tr>
                <tr><td>TELEPHONE</td><td>${supervisor_telephone}</td></tr>
            </table>

        <h2>Changes and updates to the Privacy Policy</h2>
        <p>This service may change from time to time and this Privacy Policy will be updated appropriately. We reserve the right to amend the Privacy Policy at any time – in such case, an updated version of the Privacy Policy will be posted on our website/at this location and we encourage you to check it from time to time. <#if reminders>We may email periodic reminders of our notices and terms that are currently in effect and any changes that may have been made to them.</#if></p>
        <p><!--ISKOMBINOVATI - OVAKO:-->
            ${service_owner} keeps this Privacy Policy under regular review; the last update was on (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}
            <!-- ILI OVAKO: This Privacy Policy is subject to revision. -->
        </p>
        <!-- <p>Date of last revision (yyyy-mm-dd): ${policy_date?date?string("yyyy-MM-dd")}</p> -->
 

        <!-- GENERATED FOOTER -->
 
        <div class="generated_footer">
            <p><i>Created by <a href="${repol_url}" target="_blank">RePol (${repol_url})</a> version ${repol_version}, <a href="https://github.com/RestlessDevil/RePol" target="_blank">source code at GitHub</a> under <a href="https://spdx.org/licenses/EUPL-1.2.html" target="_blank">EUPL-1.2</a>, on ${current_time?string("yyyy-MM-dd HH:mm zzz")}</i></p>
        </div>
    </body>
</html>

