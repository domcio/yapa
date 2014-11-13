package pl.edu.agh.yapa.search;

import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class SearchQueryTest {

    @Test
    public void testSingleWord() throws Exception {
        // given
        String input = "blabla";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getPhrasesToMatch()).hasSize(1);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
        assertThat(query.getWordsToExclude()).isEmpty();
    }

    @Test
    public void testMultipleWords() throws Exception {
        // given
        String input = "blabla wiwi bibi";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getPhrasesToMatch()).hasSize(3);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
        assertThat(query.getPhrasesToMatch()).contains("wiwi");
        assertThat(query.getPhrasesToMatch()).contains("bibi");
        assertThat(query.getWordsToExclude()).isEmpty();
    }

    @Test
    public void testWorkAndPhrase() throws Exception {
        // given
        String input = "blabla \"where do you go\"";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getPhrasesToMatch()).hasSize(2);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
        assertThat(query.getPhrasesToMatch()).contains("where do you go");
        assertThat(query.getWordsToExclude()).isEmpty();
    }

    @Test
    public void testBasicExclude() throws Exception {
        // given
        String input = "blabla -kuku";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getWordsToExclude()).hasSize(1);
        assertThat(query.getWordsToExclude()).contains("kuku");
        assertThat(query.getPhrasesToMatch()).hasSize(1);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
    }

    @Test
    public void testFalseExclude() throws Exception {
        // given
        String input = "blabla \"-where do you go\"";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getWordsToExclude()).isEmpty();
        assertThat(query.getPhrasesToMatch()).hasSize(2);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
        assertThat(query.getPhrasesToMatch()).contains("-where do you go");
    }

    @Test
    public void testStripWhitespace() throws Exception {
        // given
        String input = "blabla       no          bla           ";

        // when
        SearchQuery query = new SearchQuery(input);

        // then
        assertThat(query.getWordsToExclude()).isEmpty();
        assertThat(query.getPhrasesToMatch()).hasSize(3);
        assertThat(query.getPhrasesToMatch()).contains("blabla");
        assertThat(query.getPhrasesToMatch()).contains("no");
        assertThat(query.getPhrasesToMatch()).contains("bla");
    }

}