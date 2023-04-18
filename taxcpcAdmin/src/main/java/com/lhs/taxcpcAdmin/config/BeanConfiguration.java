package com.lhs.taxcpcAdmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lhs.javautilities.LhsDateUtility;
import com.lhs.javautilities.LhsEncryptionDecryptionUtility;
import com.lhs.javautilities.LhsFileUtility;
import com.lhs.javautilities.LhsStringUtility;

@Configuration
public class BeanConfiguration {

	@Bean(name = "stringUtil")
	public LhsStringUtility lhsStringUtility() {
		return new LhsStringUtility();
	}

	@Bean(name = "fileUtil")
	public LhsFileUtility lhsFileUtility() {
		return new LhsFileUtility();
	}

	@Bean(name = "encDecUtil")
	public LhsEncryptionDecryptionUtility lhsEncryptionDecryptionUtility() {
		return new LhsEncryptionDecryptionUtility();
	}
	
	@Bean("lhsDateUtility")
	public LhsDateUtility getLhsDateUtility() {
		return new LhsDateUtility();
	}

	/**
	 * A Bean for finding out and return a base location of Taxcpc properties file.
	 * 
	 * @return
	 */
//	@Bean
//	@Qualifier("dataSourceFilePath")
//	public String getDataSourceFilePath() {
//		String dirPath = "";
//		try {
//			String path = "TAXCPC" + File.separator + "DB_CONFIG" + File.separator;
//			List<File> roots = Arrays.asList(File.listRoots());
//
//			dirPath = roots.stream()
//					.filter(drive -> Files.exists(Paths.get(drive + path)))
//					.findFirst()
//					.map(dir -> dir.toString() + path)
//					.get();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return dirPath;
//	}

}
