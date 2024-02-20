package eu.avalonya.api.command;

import java.util.List;

public abstract class Argument<T>
{
    private String error;
    private boolean required = false;
    private String input;

    public abstract boolean test(String input);
    public abstract T get();

    public void setErrorMessage(String error)
    {
        this.error = error;
    }

    public String getErrorMessage()
    {
        return error;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public boolean isRequired()
    {
        return required;
    }

    public List<String> getCompletions()
    {
        return List.of();
    }

    public void setInput(String input)
    {
        this.input = input;
    }

    public String getInput()
    {
        return input;
    }
}
