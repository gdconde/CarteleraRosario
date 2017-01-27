package com.hitherejoe.mvpboilerplate;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.apps.secrets.test.common.TestComponentRule;
import com.google.android.apps.secrets.test.common.TestDataFactory;
import com.hitherejoe.mvpboilerplate.data.model.NamedResource;
import com.hitherejoe.mvpboilerplate.data.model.Pokemon;
import com.hitherejoe.mvpboilerplate.ui.main.MainActivity;
import com.hitherejoe.mvpboilerplate.util.ErrorTestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Single;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public final TestComponentRule component =
            new TestComponentRule(InstrumentationRegistry.getTargetContext());
    public final ActivityTestRule<MainActivity> main =
            new ActivityTestRule<>(MainActivity.class, false, false);

    // TestComponentRule needs to go first to make sure the Dagger ApplicationTestComponent is set
    // in the Application before any Activity is launched.
    @Rule
    public TestRule chain = RuleChain.outerRule(component).around(main);

    @Test
    public void checkPokemonsDisplay() {
        List<NamedResource> namedResourceList = TestDataFactory.makeNamedResourceList(5);
        List<String> pokemonList = TestDataFactory.makePokemonNameList(namedResourceList);
        stubDataManagerGetPokemonList(Single.just(pokemonList));
        main.launchActivity(null);

        for (NamedResource pokemonName : namedResourceList) {
            onView(withText(pokemonName.name))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void clickingPokemonLaunchesDetailActivity() {
        List<NamedResource> namedResourceList = TestDataFactory.makeNamedResourceList(5);
        List<String> pokemonList = TestDataFactory.makePokemonNameList(namedResourceList);
        stubDataManagerGetPokemonList(Single.just(pokemonList));
        stubDataManagerGetPokemon(Single.just(TestDataFactory.makePokemon("id")));
        main.launchActivity(null);

        onView(withText(pokemonList.get(0)))
                .perform(click());

        onView(withId(R.id.image_pokemon))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkErrorViewDisplays() {
        stubDataManagerGetPokemonList(Single.<List<String>>error(new RuntimeException()));
        main.launchActivity(null);
        ErrorTestUtil.checkErrorViewsDisplay();
    }

    public void stubDataManagerGetPokemonList(Single<List<String>> single) {
        when(component.getMockDataManager().getPokemonList(anyInt()))
                .thenReturn(single);
    }

    public void stubDataManagerGetPokemon(Single<Pokemon> single) {
        when(component.getMockDataManager().getPokemon(anyString()))
                .thenReturn(single);
    }

}