package org.kodein.samples.di.tornadofx.person.view

import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.kodein
import org.kodein.samples.di.tornadofx.person.controller.PersonListController
import org.kodein.samples.di.tornadofx.person.model.Person
import tornadofx.View
import tornadofx.column
import tornadofx.onUserSelect
import tornadofx.tableview

class PersonListView : View() {
    private val listController: PersonListController by kodein().instance()

    override val root = tableview(listController.personList) {
        column("ID", Person::idProperty)
        column("Firstname", Person::firstnameProperty)
        column("Lastname", Person::lastnameProperty)
        column("Birthday", Person::birthdayProperty)

        onUserSelect(2) {
                listController.editPerson(it)
        }
    }
}