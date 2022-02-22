# RePol – Repository Policy Generator

RePol (Repository Policy Generator) is an open-source web application that guides users through the process of defining policies for repositories and web-based services. It helps in defining and maintaining comprehensive and clear repository and privacy policies. Generated privacy policies are suitable for any kind of service. RePol uses a step-by-step wizard and self-explanatory forms to guide users through the process. By choosing options in a form, users shape a policy document with predefined policy clauses formulated in line with the current best practice. The resulting policy document may be downloaded as an XML file, additionally customized, and integrated into the service or repository. The collected data and the key elements of the generated policy are provided in a machine-readable format. This allows for an automated interpretation of created policies and extraction of repository-level metadata for inclusion in registries, catalogues and various operational and data discovery tools. The resulting policy document may be downloaded, additionally edited, and integrated into a repository.

The main purpose of this extensible web application is to generate policies for repositories, but it can be configured to generate any other type of document, due to the versatile nature of its configurable forms and document templates. In addition to the creation of repository policies, RePol helps to author generic privacy policies suitable for any kind of service.

This tool helps resource owners in the creation of repository and service policies and alignment of their services with requirements for participation and onboarding in the above-mentioned infrastructures.

## Install
Freemarker templates are in dedicated directory in `Maven` `resources` directory. Default (relative) path for template directory is `freemarker_templates` and it can be changed in `RePol/src/main/resources/config/settings.cfg` file.
Default templates are provided in `src/main/resources/fm_templates`.

```
cd RePol
mvn package
cd target
mv repol-x.x-SNAPSHOT.war $TOMCAT_PATH/webapps/
```

## Customization of forms
All forms are defined in the xml file `RePol/src/main/resources/config/template-forms.xml`. Attribute `id` of the form node must match the file name of the corresponding template.
Lists are defined in `RePol/src/main/resource/config/selection-list.xml` file and are referenced by `list-id` of the input elements which use lists of pre-defined values (poolpicker, selectone, selectmany).

## Project Wiki
Visit [RePol Wiki](https://wiki.ni4os.eu/index.php/RePol).

## License
* European Union Public License, version 1.2 or later, ([LICENSE](LICENSE) or https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12)

The EUPL is a **copyleft, GPL-compatible license** managed by the European Union, with legally-equal translated versions **in all languages of the EU**. See [this introduction](https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence) for information about the purpose, objectives and translations of the license. See also the [license compatibility matrix](https://joinup.ec.europa.eu/collection/eupl/matrix-eupl-compatible-open-source-licences).

### Credits
Up to 2022 RePol was developed and hosted by [Vasilije Rajović](https://github.com/RestlessDevil) on behalf of RCUB.
