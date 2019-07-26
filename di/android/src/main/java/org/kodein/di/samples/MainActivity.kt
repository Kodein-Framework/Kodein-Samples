package org.kodein.di.samples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.*
import org.kodein.di.android.*
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.samples.coffee.Coffee
import org.kodein.di.samples.coffee.Kettle
import org.kodein.di.samples.coffee.thermosiphonModule

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein by retainedSubKodein(kodein(), copy = Copy.All) {
        import(thermosiphonModule)
    }

    // will be the same instance as the coffeeMaker in MainFragment
    val coffeeMaker: Kettle<Coffee> by instance()
    val log: AndroidLogger by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            log.log("Going to brew coffee using $coffeeMaker")

            supportFragmentManager.beginTransaction().add(
                R.id.fragment,
                MainFragment()
            ).commit()
        }

        Log.i("Kodein", "=====================-BINDINGS-=====================")
        Log.i("Kodein", kodein.container.tree.bindings.description())
        Log.i("Kodein", "====================================================")
    }

}
