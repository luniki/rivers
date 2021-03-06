/**
 *
 */
package rhein;

/**
 * @author mlunzena
 * 
 */
public class RetentionBasinGenerator {

	/**
   *
   */
	protected double meanCostFactor = 4 * 86000 / 1000;

	/**
   *
   */
	protected long meanRetentionAmount = 10000000 / 86000;

	/**
   *
   */
	protected double probability = .1;

	/**
   *
   */
	protected double stdDevCostFactor = 0.5 * 86000 / 1000;

	/**
   *
   */
	protected long stdDevRetentionAmount = 2000000 / 86000;

	/**
	 * @return
	 */
	public RetentionBasin generateRetentionBasin() {

		double p = RheinHelper.nextUniform();
		double r = RheinHelper.nextNormalDouble();

		if (!(p < probability)) {
			return null;
		}

		double costFactor = meanCostFactor + r * stdDevCostFactor;
		int capacity = (int) (meanRetentionAmount + r * stdDevRetentionAmount);

		return new RetentionBasin(capacity, costFactor);
	}
}
