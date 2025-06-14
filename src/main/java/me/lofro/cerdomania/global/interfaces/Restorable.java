package me.lofro.cerdomania.global.interfaces;

import me.lofro.data.utils.JsonConfig;

/**
 * An interface that holds common methods for all Restore-able Managers.
 */
public interface Restorable {

    /**
     * Method to contain your restore logic.
     * 
     * @param jsonConfig The {@link JsonConfig} object to read from.
     */
    void restore(JsonConfig jsonConfig);

    /**
     * Default method to be implemented by a child class that needs storing of state
     * to json files.
     * 
     * @param jsonConfig The {@link JsonConfig} object to save to.
     */
    void save(JsonConfig jsonConfig);

}
