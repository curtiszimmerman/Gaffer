/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gaffer.operation.handler;

import com.google.common.collect.Lists;
import gaffer.commonutil.iterable.CloseableIterable;
import gaffer.commonutil.iterable.LimitedCloseableIterable;
import gaffer.commonutil.iterable.WrappedCloseableIterable;
import gaffer.operation.impl.Limit;
import gaffer.store.operation.handler.LimitHandler;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LimitHandlerTest {
    @Test
    public void shouldLimitResults() throws Exception {
        // Given
        final List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
        final List<Integer> expectedResult = Arrays.asList(1, 2, 3);
        final int resultLimit = expectedResult.size();
        final Limit<Integer> limit = new Limit.Builder<Integer>()
                .input(input)
                .limitResults(resultLimit)
                .build();

        final LimitHandler<Integer> handler = new LimitHandler<>();

        // When
        final Iterable<Integer> result = handler.doOperation(limit, null, null);

        // Then
        assertTrue(result instanceof LimitedCloseableIterable);
        assertEquals(0, ((LimitedCloseableIterable) result).getStart());
        assertEquals(resultLimit, ((LimitedCloseableIterable) result).getEnd());
        assertEquals(expectedResult, Lists.newArrayList(result));
    }

    @Test
    public void shouldNotLimitResultsOfGetOperationWhenLimitIsNull() throws Exception {
        // Given
        final CloseableIterable<Integer> input = new WrappedCloseableIterable<>(Arrays.asList(1, 2, 3, 4, 5));
        final Integer resultLimit = null;
        final Limit<Integer> limit = new Limit.Builder<Integer>()
                .input(input)
                .limitResults(resultLimit)
                .build();

        final LimitHandler<Integer> handler = new LimitHandler<>();

        // When
        final Iterable<Integer> result = handler.doOperation(limit, null, null);

        // Then
        assertSame(input, result);
    }
}