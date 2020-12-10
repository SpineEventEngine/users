/*
 * Copyright 2020, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.change;

/**
 * A common interface for types defining a change of a field with the type {@code T}.
 *
 * <p>When used as a field of a command, a change instance holds a request to change a field value
 * from the {@linkplain #getPreviousValue() current one} to a {@linkplain #getNewValue() new one}:
 *
 * <pre>{@code
 * // The command to change a title of a task.
 * message ChangeTitle {
 *     TaskId task = 1;
 *     spine.change.StringChange title = 2 [(required) = true];
 * }
 * }</pre>
 *
 * <p>When used in an event, a change instance provides information about the values changed
 * in a field:
 *
 * <pre>{@code
 * // A task title changed.
 * message TitleChanged {
 *     TaskId task = 1 [(required) = true];
 *     spine.change.StringChange title = 2 [(required) = true];
 * }
 * }</pre>
 *
 * @param <T>
 *         the type of the changing field
 */
public interface Change<T> {

    /**
     * The previous value of the field.
     */
    T getPreviousValue();

    /**
     * The new value of the field.
     */
    T getNewValue();

    /**
     * The previous value of the field.
     */
    default T previousValue() {
        return getPreviousValue();
    }

    /**
     * The new value of the field.
     */
    default T newValue() {
        return getNewValue();
    }
}
