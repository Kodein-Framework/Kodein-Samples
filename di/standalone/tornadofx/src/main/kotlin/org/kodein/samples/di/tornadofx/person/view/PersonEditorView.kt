package org.kodein.samples.di.tornadofx.person.view

import javafx.beans.binding.Bindings
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import org.kodein.di.*
import org.kodein.di.tornadofx.kodeinDI
import org.kodein.di.tornadofx.subDI
import org.kodein.samples.di.tornadofx.person.model.EditingState
import org.kodein.samples.di.tornadofx.person.model.PersonRepository
import org.kodein.samples.di.tornadofx.person.model.PersonScope
import tornadofx.*

class PersonEditorView : View() {
    lateinit var tabPane: TabPane

    override val root = hbox {
        tabpane {

            val test: String by kodeinDI().instance("test")
            println("tabpane: $test")

            hboxConstraints { hGrow = Priority.ALWAYS }
            tabPane = this
        }
    }
}

class EditorTabFragment : Fragment(), DIAware {
    override val di: DI = subDI {
        constant("item") with "Person"
    }

    private val item: String by instance("item")
    private val personRepository: PersonRepository by instance()

    private val editingState: EditingState by on(this).instance()

    override val scope = super.scope as PersonScope
    val model = scope.model
    lateinit var tab: Tab

    override val root = hbox {
        val test: String by kodeinDI().instance("test")
        println("hbox: $test")

        form {
            subDI(allowSilentOverride = true) {
                constant("test") with "MyForm"
            }

            val test2: String by kodeinDI().instance("test")
            println("form: $test2")

            fieldset("Edit $item") {
                field("Firstname") {
                    textfield(model.firstname) {
                        setOnKeyReleased {
                            editingState.editing = true
                        }
                    }
                }
                field("Lastname") {
                    textfield(model.lastname)
                    setOnKeyReleased {
                        editingState.editing = true
                    }
                }
                field("Birthdate") {
                    datepicker(model.birthday)
                    setOnKeyReleased {
                        editingState.editing = true
                    }
                }

                button("Save") {
                    enableWhen(editingState.editingProperty)
                    action {
                        save()
                    }
                }
            }
        }
    }

    private fun save() {
        model.commit()
        editingState.editing = false
        tab.text = Bindings.concat(model.firstname, " ", model.lastname).valueSafe
        personRepository.save(person = model.item)
    }
}
