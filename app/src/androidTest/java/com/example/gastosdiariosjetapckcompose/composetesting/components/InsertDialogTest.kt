package com.example.gastosdiariosjetapckcompose.composetesting.components

import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import org.junit.Rule
import org.junit.Test

class InsertDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myFirstTest() {
        composeTestRule.setContent {
            AristComponent()
        }
         //FINDER
        //BUSCNADO UN COMPONENTE EN EXPESIFICIO CON FINDER
        //estamos diciendo que busuqe un nodo que teng el texto
        composeTestRule.onNodeWithText("aris", ignoreCase = true)
        //buscando por el id
        composeTestRule.onNodeWithTag("component1")
        //buscando por contentDescription
        composeTestRule.onNodeWithContentDescription("superimage", ignoreCase = true)

        //COMPROBANDO TODOS A LA VEZ
        //que me busque todoo lo que contenga
        composeTestRule.onAllNodesWithText("a")
        composeTestRule.onAllNodesWithTag("component3")
        composeTestRule.onAllNodesWithContentDescription("visualIcon")

        // ACTIONS
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performClick()
        composeTestRule.onAllNodesWithText("a", ignoreCase = true).onFirst().performClick()
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performTouchInput {
            longClick()
            doubleClick()
            swipeDown()
            swipeUp()
            swipeLeft()
            swipeRight()
        }
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performScrollTo().performClick().performTextInput("")
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performImeAction()
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performTextClearance()
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performTextInput("julio")
        composeTestRule.onNodeWithText("aris", ignoreCase = true).performTextReplacement("julito")
        //ASSERTIONS
        composeTestRule.onNodeWithText("aris", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("aris", ignoreCase = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("aris", ignoreCase = true).assertContentDescriptionContains("")
        composeTestRule.onNodeWithText("aris", ignoreCase = true).assertContentDescriptionEquals("")
        composeTestRule.onNodeWithText("aris", ignoreCase = true).assertIsDisplayed()
    }

}