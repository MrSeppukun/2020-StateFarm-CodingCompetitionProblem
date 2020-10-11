package sf.codingcompetition2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import sf.codingcompetition2020.structures.*;

public class CodingCompCsvUtil {
	
	/* #1 
	 * readCsvFile() -- Read in a CSV File and return a list of entries in that file.
	 * @param filePath -- Path to file being read in.
	 * @param classType -- Class of entries being read in.
	 * @return -- List of entries being returned.
	 */
	public <T> List<T> readCsvFile(String filePath, Class<T> classType) {
		try {
			Scanner scan = new Scanner(new File(filePath));

			List<T> objectList = new <T>ArrayList<T>();
			scan.nextLine();

			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				LinkedList<String> lineData = new LinkedList<String>(Arrays.asList(line.split(",")));
				if (Agent.class.equals(classType)) {
					Agent agent = new Agent(Integer.parseInt(lineData.get(0)), lineData.get(1), lineData.get(2), lineData.get(3), lineData.get(4));

					objectList.add((T) agent);
				} else if (classType.equals(Claim.class)) {
					Claim claim = new Claim(Integer.parseInt(lineData.get(0)), Integer.parseInt(lineData.get(1)), Boolean.parseBoolean(lineData.get(2)), Integer.parseInt(lineData.get(3)));

					objectList.add((T) claim);
				} else if (classType.equals(Vendor.class)) {
					Vendor vendor = new Vendor(Integer.parseInt(lineData.get(0)), lineData.get(1), Integer.parseInt(lineData.get(2)), Boolean.parseBoolean(lineData.get(3)));

					objectList.add((T) vendor);
				} else if (classType.equals(Customer.class)) {

					List<Dependent> dependentList = new ArrayList<Dependent>();

					if (!lineData.get(8).isBlank()) {
						String tempString ="";

						while (lineData.get(8).contains("\"")) {

							tempString += lineData.get(8);
							tempString += ",";
							lineData.remove(8);
						}

						LinkedList<String> dependentData = new LinkedList<String>(Arrays.asList(tempString.split("\"\"")));


						List<String> names = new ArrayList<String>();
						for (int i = 3; i < dependentData.size(); i += 4) {
							names.add(dependentData.get(i));
						}


						for (int i = 0; i < names.size(); i += 2) {
							Dependent dependent = new Dependent(names.get(i), names.get(i + 1));
							dependentList.add(dependent);
						}
						Customer customer = new Customer(Integer.parseInt(lineData.get(0)), lineData.get(1), lineData.get(2), Integer.parseInt(lineData.get(3)), lineData.get(4), Integer.parseInt(lineData.get(5)), Short.parseShort(lineData.get(6)), lineData.get(7), dependentList, Boolean.parseBoolean(lineData.get(8)), Boolean.parseBoolean(lineData.get(9)), Boolean.parseBoolean(lineData.get(10)), lineData.get(11), Short.parseShort(lineData.get(12)), Integer.parseInt(lineData.get(13)));
						objectList.add((T) customer);
					}

					else {
						List<Dependent> dependentList2 = new ArrayList<Dependent>();
						Customer customer = new Customer(Integer.parseInt(lineData.get(0)), lineData.get(1), lineData.get(2), Integer.parseInt(lineData.get(3)), lineData.get(4), Integer.parseInt(lineData.get(5)), Short.parseShort(lineData.get(6)), lineData.get(7), dependentList2 , Boolean.parseBoolean(lineData.get(9)), Boolean.parseBoolean(lineData.get(10)), Boolean.parseBoolean(lineData.get(11)), lineData.get(12), Short.parseShort(lineData.get(13)), Integer.parseInt(lineData.get(14)));
						objectList.add((T) customer);
					}



				}
			}
			return objectList;
		}
		catch (FileNotFoundException e){
			e.printStackTrace();

		}
		return null;
	}


	/* #2
	 * getAgentCountInArea() -- Return the number of agents in a given area.
	 * @param filePath -- Path to file being read in.
	 * @param area -- The area from which the agents should be counted.
	 * @return -- The number of agents in a given area
	 */
	public int getAgentCountInArea(String filePath,String area) {
		List<Agent> agentList = readCsvFile(filePath, Agent.class);
		int sum = 0;

		for (int i = 0; i < agentList.size(); i++) {
			if (agentList.get(i).getArea().equals(area)) {
				sum++;
			}
		}

		return sum;
		//return csvFile;
	}

	
	/* #3
	 * getAgentsInAreaThatSpeakLanguage() -- Return a list of agents from a given area, that speak a certain language.
	 * @param filePath -- Path to file being read in.
	 * @param area -- The area from which the agents should be counted.
	 * @param language -- The language spoken by the agent(s).
	 * @return -- The number of agents in a given area
	 */
	public List<Agent> getAgentsInAreaThatSpeakLanguage(String filePath, String area, String language) {
		List<Agent> agentList = readCsvFile(filePath, Agent.class);
		List<Agent> agentMatchList = new ArrayList<Agent>();

		for (int i = 0; i < agentList.size(); i++) {
			if (agentList.get(i).getArea().equals(area)) {
				if(agentList.get(i).getLanguage().equals(language)) {
					agentMatchList.add(agentList.get(i));
				}
			}
		}
		return agentMatchList;
	}
	
	
	/* #4
	 * countCustomersFromAreaThatUseAgent() -- Return the number of individuals from an area that use a certain agent.
	 * @param filePath -- Path to file being read in.
	 * @param customerArea -- The area from which the customers should be counted.
	 * @param agentFirstName -- First name of agent.
	 * @param agentLastName -- Last name of agent.
	 * @return -- The number of customers that use a certain agent in a given area.
	 */
	public short countCustomersFromAreaThatUseAgent(Map<String,String> csvFilePaths, String customerArea, String agentFirstName, String agentLastName) {

		List<Agent> agentList= readCsvFile(csvFilePaths.get("agentList"), Agent.class);
		List<Customer> customerList = readCsvFile(csvFilePaths.get("customerList"), Customer.class);
		short sum = 0;

		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getArea().equals(customerArea)) {
				int agentId = 0;
				for (int j = 0; j < agentList.size(); j++) {
					if (agentList.get(j).getFirstName().equals(agentFirstName) && agentList.get(j).getLastName().equals(agentLastName)) {
						agentId = agentList.get(j).getAgentId();
					}
				}
				if (agentId == customerList.get(i).getAgentId()) {
					sum++;
				}
			}
		}

		return sum;
	}

	
	/* #5
	 * getCustomersRetainedForYearsByPlcyCostAsc() -- Return a list of customers retained for a given number of years, in ascending order of their policy cost.
	 * @param filePath -- Path to file being read in.
	 * @param yearsOfServeice -- Number of years the person has been a customer.
	 * @return -- List of customers retained for a given number of years, in ascending order of policy cost.
	 */
	public List<Customer> getCustomersRetainedForYearsByPlcyCostAsc(String customerFilePath, short yearsOfService) {
		List<Customer> customerList = readCsvFile(customerFilePath, Customer.class);
		List<Customer> validCustomers = new ArrayList<Customer>();

		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getYearsOfService() == yearsOfService) {
				validCustomers.add(customerList.get(i));
			}
		}

		int tempMin = Integer.parseInt(validCustomers.get(0).getTotalMonthlyPremium().substring(1));
		int tempMinIndex = 0;
		List<Customer> sortedCustomers = new ArrayList<Customer>();
		int size = validCustomers.size();

		for (int i = 0; i < size; i++) {
			tempMin = Integer.MAX_VALUE;
			for (int j = 0; j < validCustomers.size(); j++) {


				if (Integer.parseInt(validCustomers.get(j).getTotalMonthlyPremium().substring(1)) < tempMin) {

					tempMin = Integer.parseInt(validCustomers.get(j).getTotalMonthlyPremium().substring(1));
					tempMinIndex = j;


				}
			}

			sortedCustomers.add(validCustomers.get(tempMinIndex));
			validCustomers.remove(tempMinIndex);
		}

		return sortedCustomers;
	}

	
	/* #6
	 * getLeadsForInsurance() -- Return a list of individuals who’ve made an inquiry for a policy but have not signed up.
	 * *HINT* -- Look for customers that currently have no policies with the insurance company.
	 * @param filePath -- Path to file being read in.
	 * @return -- List of customers who’ve made an inquiry for a policy but have not signed up.
	 */
	public List<Customer> getLeadsForInsurance(String filePath) {
		List<Customer> customerList = readCsvFile(filePath, Customer.class);
		List<Customer> potentialCustomers = new ArrayList<Customer>();

		for (int i = 0; i < customerList.size(); i++) {
			if (!customerList.get(i).isAutoPolicy() && !customerList.get(i).isHomePolicy() && !customerList.get(i).isRentersPolicy()) {
				potentialCustomers.add(customerList.get(i));
			}
		}

		return potentialCustomers;
	}


	/* #7
	 * getVendorsWithGivenRatingThatAreInScope() -- Return a list of vendors within an area and include options to narrow it down by: 
			a.	Vendor rating
			b.	Whether that vendor is in scope of the insurance (if inScope == false, return all vendors in OR out of scope, if inScope == true, return ONLY vendors in scope)
	 * @param filePath -- Path to file being read in.
	 * @param area -- Area of the vendor.
	 * @param inScope -- Whether or not the vendor is in scope of the insurance.
	 * @param vendorRating -- The rating of the vendor.
	 * @return -- List of vendors within a given area, filtered by scope and vendor rating.
	 */
	public List<Vendor> getVendorsWithGivenRatingThatAreInScope(String filePath, String area, boolean inScope, int vendorRating) {
		List<Vendor> vendorList = readCsvFile(filePath, Vendor.class);
		List<Vendor> validList = new ArrayList<Vendor>();


		for (int i = 0; i < vendorList.size(); i++) {
			if (vendorList.get(i).getVendorRating() >= vendorRating && vendorList.get(i).getArea().equals(area)) {
				if (inScope) {
					if (vendorList.get(i).isInScope()) {
						validList.add(vendorList.get(i));
					}
				}

				else {
					validList.add(vendorList.get(i));
				}
			}
		}

		return validList;
	}


	/* #8
	 * getUndisclosedDrivers() -- Return a list of customers between the age of 40 and 50 years (inclusive), who have:
			a.	More than X cars
			b.	less than or equal to X number of dependents.
	 * @param filePath -- Path to file being read in.
	 * @param vehiclesInsured -- The number of vehicles insured.
	 * @param dependents -- The number of dependents on the insurance policy.
	 * @return -- List of customers filtered by age, number of vehicles insured and the number of dependents.
	 */
	public List<Customer> getUndisclosedDrivers(String filePath, int vehiclesInsured, int dependents) {
		List<Customer> customerList = readCsvFile(filePath, Customer.class);
		List<Customer> filteredList = new ArrayList<Customer>();

		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getAge() >= 40 && customerList.get(i).getAge() <= 50) {
				if (customerList.get(i).getVehiclesInsured() > vehiclesInsured) {

					if (customerList.get(i).getDependents().size() <= dependents) {
						filteredList.add(customerList.get(i));
					}
				}
			}
		}

		return filteredList;
	}	


	/* #9
	 * getAgentIdGivenRank() -- Return the agent with the given rank based on average customer satisfaction rating. 
	 * *HINT* -- Rating is calculated by taking all the agent rating by customers (1-5 scale) and dividing by the total number 
	 * of reviews for the agent.
	 * @param filePath -- Path to file being read in.
	 * @param agentRank -- The rank of the agent being requested.
	 * @return -- Agent ID of agent with the given rank.
	 */
	public int getAgentIdGivenRank(String filePath, int agentRank) {
			return 0;
	}	

	
	/* #10
	 * getCustomersWithClaims() -- Return a list of customers who’ve filed a claim within the last <numberOfMonths> (inclusive). 
	 * @param filePath -- Path to file being read in.
	 * @param monthsOpen -- Number of months a policy has been open.
	 * @return -- List of customers who’ve filed a claim within the last <numberOfMonths>.
	 */
	public List<Customer> getCustomersWithClaims(Map<String,String> csvFilePaths, short monthsOpen) {
		List<Claim> claimList = readCsvFile(csvFilePaths.get("claimList"), Claim.class);
		List<Customer> customerList = readCsvFile(csvFilePaths.get("customerList"), Customer.class);

		List<Customer> validCustomers = new ArrayList<Customer>();
		int customerId = 0;

		for (int i = 0; i < claimList.size(); i++) {
			//customerId = 0;
			if (claimList.get(i).getMonthsOpen() <= monthsOpen) {
				customerId = claimList.get(i).getCustomerId();

				System.out.println(customerId);
					for (int j = 0; j < customerList.size(); j++) {
						if (customerId == customerList.get(j).getCustomerId()) {
							validCustomers.add(customerList.get(j));
						}
					}
			}
		}


		return validCustomers;
	}	

}
