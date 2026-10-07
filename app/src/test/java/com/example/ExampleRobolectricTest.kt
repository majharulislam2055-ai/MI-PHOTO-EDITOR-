package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("MI PHOTO EDITOR", appName)
  }

  @Test
  fun `test bilingual strings dictionary`() {
    val bnTitle = com.example.model.Strings.get("app_title", com.example.model.AppLanguage.BN)
    val enTitle = com.example.model.Strings.get("app_title", com.example.model.AppLanguage.EN)
    assertEquals("MI PHOTO EDITOR", bnTitle)
    assertEquals("MI PHOTO EDITOR", enTitle)
  }

  @Test
  fun `test default photo edit state values`() {
    val state = com.example.model.PhotoEditState()
    assertEquals(0f, state.lighting.brightness, 0.01f)
    assertEquals(0f, state.face.skinSmooth, 0.01f)
    assertEquals("none", state.filter.filterId)
  }
}
