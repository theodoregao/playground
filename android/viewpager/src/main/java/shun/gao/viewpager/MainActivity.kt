package shun.gao.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import shun.gao.viewpager.city.CityFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CityFragment.newInstance())
                .commitNow()
        }
    }
}