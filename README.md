## Install
Make sure to select the path for template directory. Default is `/usr/local/fm_templates` and it can be changed in `RePol/src/main/resources/config/settings.cfg` file.
Default templates are provided in `RePol/fm_templates`.

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
