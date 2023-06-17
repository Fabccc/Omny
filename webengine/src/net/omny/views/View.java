package net.omny.views;

import net.omny.route.Response;

/**
 * Represents a view
 *
 * Can also be used as a functionnal interface
 * that implements the write method
 *
 */
@FunctionalInterface
public interface View {

  /**
   *
   * Write to the passed buffer
   *
   * You must flip the buffer before ending the function
   * @author Fabien CAYRE (Computer)
   *
   * @param buffer
   * @date 17/08/2021
   */
  void write(Response res);
}
