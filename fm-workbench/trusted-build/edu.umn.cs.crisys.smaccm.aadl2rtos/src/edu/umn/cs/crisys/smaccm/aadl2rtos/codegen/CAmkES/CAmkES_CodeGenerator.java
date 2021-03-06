package edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.CAmkES;

/**
 * CAmkES Code generator class
 * 
 * "top level" writer for CAmkES code generator.  
 * Uses StringTemplate .stg files to generate code for CAmkES.
 * 
 * TODO: normalize the interface for name prefixes.  It seems to 
 * be "smaccm_", but this is not uniformly applied.
 * 
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import edu.umn.cs.crisys.smaccm.aadl2rtos.Aadl2RtosFailure;
import edu.umn.cs.crisys.smaccm.aadl2rtos.Logger;
import edu.umn.cs.crisys.smaccm.aadl2rtos.util.Util;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.common.*;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.names.ModelNames;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.names.RemoteProcedureGroupNames;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.names.ThreadCalendarNames;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.names.ThreadImplementationNames;
import edu.umn.cs.crisys.smaccm.aadl2rtos.codegen.names.TypeNames;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.port.InputDataPort;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.port.InputEventPort;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.port.InputPeriodicPort;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.port.OutputDataPort;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.port.OutputEventPort;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.rpc.RemoteProcedureGroup;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.thread.SharedData;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.thread.ThreadImplementation;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.type.Type;
import edu.umn.cs.crisys.smaccm.aadl2rtos.model.type.UnitType;
import edu.umn.cs.crisys.smaccm.aadl2rtos.parse.Model;


public class CAmkES_CodeGenerator extends CodeGeneratorBase {
	private File interfacesDirectory;
	private File makeTemplateDirectory; 

  protected File getGlobalIncludeDirectory(File rootDirectory) {
    return new File(rootDirectory, "include");
  }
  
  protected File getGlobalTemplateDirectory(File rootDirectory) {
    return new File(rootDirectory, "make_template");
  }
  protected File getGlobalComponentDirectory(File rootDirectory) {
    return new File(rootDirectory, "components");
  }
  
  protected File getComponentDirectory(File globalComponentDirectory, String name) {
    return new File(globalComponentDirectory, name);
  }
  protected File getComponentHeaderDirectory(File componentDirectory) {
    return new File(componentDirectory, "include");
  }
  protected File getComponentSourceDirectory(File componentDirectory) {
    return new File(componentDirectory, "src");
  }

	public CAmkES_CodeGenerator(Logger log, Model model, File aadlDirectory, File outputDir) {
	  super(log, model, aadlDirectory, outputDir, "Camkes");

    makeTemplateDirectory = 
      new File(outputDirectory, "make_template");
    makeTemplateDirectory.mkdirs();

		interfacesDirectory = 
		    new File(outputDirectory, "interfaces");
		interfacesDirectory.mkdirs();
		System.out.println(interfacesDirectory.getAbsolutePath());

	}
	
  public void createReadWriteIdlInterface(Type t) throws Aadl2RtosFailure {
    TypeNames type = new TypeNames(t); 
    ModelNames m = new ModelNames(model); 

    writeGeneric(interfacesDirectory, "Idl4ReaderWriter.stg", "idlProc", 
        new String[] {"type", "model"}, 
        new Object[] {type, m}, 
        type.getReaderWriterInterfaceName(), false, type.getReaderWriterIdlFileName());
  }

  public void createReadWriteIdlInterfaces() throws Aadl2RtosFailure {
    for (Type t : getUserTypes()) {
      createReadWriteIdlInterface(t); 
    }
  }
/*  protected void writeGeneric(File directory, String templateFileName, String templateName, String tlTemplateArg[], Object tlNamesClass[], 
      String headerFooterName, boolean headerUsesDT, String fileName) throws Aadl2RtosFailure {
*/  
  public void createRpcIdlInterface(RemoteProcedureGroup rpg) throws Aadl2RtosFailure {
    RemoteProcedureGroupNames rpgn = new RemoteProcedureGroupNames(rpg); 
    ModelNames m = new ModelNames(model); 
    writeGeneric(interfacesDirectory, "Idl4Rpc.stg", "rpgDeclaration", 
        new String[] {"remoteProcedureGroup", "model"}, 
        new Object[] {rpgn, m}, 
        rpgn.getName(), false, rpgn.getIdlName());
  }
  
  public void createRpcIdlInterfaces() throws Aadl2RtosFailure {
    for (RemoteProcedureGroup t : model.getRemoteProcedureGroupMap().values()) {
      createRpcIdlInterface(t); 
    }
  }

  public Set<Type> getSharedVariableTypes() {
    // write dispatcher types
    Set<Type> svTypeSet = new HashSet<Type>();
    
    for (SharedData d : model.getSharedDataList()) {
       svTypeSet.add(d.getType());
    }
    return svTypeSet ; 
  }

  public void createSharedVariableIdlInterfaces() throws Aadl2RtosFailure {
    ModelNames m = new ModelNames(model); 
    
    for (Type t : getSharedVariableTypes()) {
      TypeNames type = new TypeNames(t);
      writeGeneric(interfacesDirectory, "Idl4SharedVar.stg", "svProc", 
          new String[] {"type", "model"}, 
          new Object[] {type, m}, 
          type.getSharedDataInterfaceName(), false, type.getSharedDataIdlFileName());
    }
  }


  public void createDispatchInterface(ThreadImplementation ti) throws Aadl2RtosFailure {
    ThreadImplementationNames tin = new ThreadImplementationNames(ti);
    ModelNames m = new ModelNames(model); 
    writeGeneric(interfacesDirectory, "Idl4Dispatch.stg", "dispatcherProc", 
        new String[] {"threadImpl", "datatypesHeader"}, 
        new Object[] {tin, m.getSystemTypeHeaderName()}, 
        tin.getIdlName(), false, tin.getIdlFileName());
	}
	

	
  // For a given thread implementation, how do we connect it to the other components?
  // We can go from a thread implementation to a thread instance.  In our case (for now), 
  // There is only one thread instance per implementation.  So, we get the thread 
  // instance, follow the connections to other thread instances, and map back up to the 
  // other thread implementations.  From here, we can grab the idl file for that thread implementation.  

	public void createComponentCamkesFile(File componentDirectory, ThreadImplementation ti) throws Aadl2RtosFailure {
      ThreadImplementationNames tin = new ThreadImplementationNames(ti); 
	  String fname = tin.getComponentCamkesFileName(); 
	  if (ti.getIsExternal()) { fname += ".template"; }
    writeGeneric(componentDirectory, "ComponentCamkes.stg", "componentCamkesBody", 
        "threadImpl", tin, 
        tin.getComponentName(), false, fname);
	}

	@Override
  public void osSpecificComponentFiles(ThreadImplementation ti, 
      File componentDirectory, 
      File srcDirectory, File includeDirectory) throws Aadl2RtosFailure {
	  createDispatchInterface(ti);
    createComponentCamkesFile(componentDirectory, ti);
  }
		
  public void createClockDriver(File srcDirectory, File includeDirectory) throws Aadl2RtosFailure {
	  
    String concrete_driver = null; 
    if (model.getHWTarget().equalsIgnoreCase("QEMU")) {
      concrete_driver = "qemu_clock_driver.c";
    } else if (model.getHWTarget().equalsIgnoreCase("ODROID")) {
      concrete_driver = "odroid_clock_driver.c";
    }
    else {
      log.warn("Clock driver for HW platform: " + model.getHWTarget() + " is currently unimplemented.  " + 
          "Please implement interface as specified in clock_driver.h for this platform, and place the resulting .c file in the dispatch_periodic directory.");
    }
    
    InputStream cSrcFileStream = null;
    InputStream hSrcFileStream = null;
    OutputStream cDstFileStream = null;
    OutputStream hDstFileStream = null;
    
    // write the .c / .h files to the destination component
    try {
      try {
        if (concrete_driver != null) {
          File cdest = new File(srcDirectory, concrete_driver);
          cSrcFileStream = Util.findConfigFile(concrete_driver);
          cDstFileStream = new FileOutputStream(cdest);
          copyFile(cSrcFileStream, cDstFileStream);
        }
        
        File hdest = new File(includeDirectory, "clock_driver.h");
        hSrcFileStream = Util.findConfigFile("clock_driver.h");
        hDstFileStream = new FileOutputStream(hdest); 
        copyFile(hSrcFileStream, hDstFileStream);
        
      } catch (IOException ioe) {
        log.error("IOException occurred during clock driver write: " + ioe);
        log.error("Continuing anyway...");
        // throw new Aadl2RtosFailure();
      } finally {
        if (cSrcFileStream != null) { cSrcFileStream.close(); }
        if (hSrcFileStream != null) { hSrcFileStream.close(); }
        if (cDstFileStream != null) { cDstFileStream.close(); }
        if (hDstFileStream != null) { hDstFileStream.close(); }
      }
    } catch (IOException ioe) {
      log.error("IOException occurred during clock driver close: " + ioe);
      throw new Aadl2RtosFailure();
    }
  }
  
  public void createPeriodicDispatcherCFile(File srcDirectory) throws Aadl2RtosFailure {
    ModelNames mn = new ModelNames(model);
    ThreadCalendarNames tcn = new ThreadCalendarNames(model.getThreadCalendar());

    writeGeneric(srcDirectory, "PeriodicDispatcherC.stg", "periodicComponentCBody", 
        new String[] {"model", "threadCalendar"}, 
        new Object[] {mn, tcn}, 
        tcn.getPeriodicDispatcherComponentName(), false, tcn.getPeriodicDispatcherCFileName());
  }
	
	// create this only if we have periodic threads.
	
	public void createPeriodicDispatcherComponent() throws Aadl2RtosFailure {
	  ModelNames mn = new ModelNames(model); 
    TypeNames tn = new TypeNames(InputPeriodicPort.getPortType());
    
    File componentDirectory = new File(componentsDirectory, mn.getThreadCalendar().getPeriodicDispatcherComponentName());
    componentDirectory.mkdirs();
  
    File srcDirectory = new File(componentDirectory, "src");
    srcDirectory.mkdirs();
    
    File includeDirectory = new File(componentDirectory, "include");
    includeDirectory.mkdirs();
    
    // MWW: removed to work with new periodic driver architecture.
    // createClockDriver(srcDirectory, includeDirectory);
    createPeriodicDispatcherCFile(srcDirectory); 
    
    writeGeneric(componentDirectory, "PeriodicDispatcherCamkes.stg", "periodicDispatcherCamkesBody", 
        new String[] {"model", "type"}, 
        new Object[] {mn, tn}, 
        mn.getThreadCalendar().getPeriodicDispatcherComponentName(), false, mn.getThreadCalendar().getPeriodicDispatcherCamkesFileName());
	}
	

	void createAssembly() throws Aadl2RtosFailure {
	  ModelNames mn = new ModelNames(model); 
    writeGeneric(outputDirectory, "Assembly.stg", "camkesAssemblyBody", "model", mn,  
        model.getSystemInstanceName(), false, mn.getCamkesSystemAssemblyFileName());
  }
	
	public void createTemplateMakefile() throws Aadl2RtosFailure {
    ModelNames mn = new ModelNames(model); 
    writeGeneric(makeTemplateDirectory, "Makefile.stg", "camkesMakefileBody", "model", mn,  
        model.getSystemInstanceName(), false, "Makefile");
	}
	
  public void createTemplateFile(String fileName, String templateName) throws Aadl2RtosFailure {
    File HFile = new File(makeTemplateDirectory, fileName);
    try (BufferedWriter hwriter = new BufferedWriter(new FileWriter(HFile))) { 
      STGroupFile stg = this.createTemplate("Makefile.stg");
      String name = getLastDir(); 
      String CapName = name.toUpperCase();
      ST st = stg.getInstanceOf(templateName);
      st.add("name", name);
      st.add("CapName", CapName);
      hwriter.append(st.render());
    } catch (IOException e) {
      log.error("IOException occurred during CAmkES write: " + e);
      throw new Aadl2RtosFailure();
    }
  }

  public void createTemplateKbuild() throws Aadl2RtosFailure {
    createTemplateFile("Kbuild", "camkesKbuild");
  }

  public void createTemplateKconfig() throws Aadl2RtosFailure {
    createTemplateFile("Kconfig", "camkesKconfig");
  }
  
	public void write() throws Aadl2RtosFailure {
	  createTypesHeader();
    createReadWriteIdlInterfaces();
    createRpcIdlInterfaces();
    createSharedVariableIdlInterfaces();
    createComponents();
    createAssembly(); 
    createTemplateMakefile(); 
    createTemplateKconfig(); 
    createTemplateKbuild();
    
    // final check for errors from string template.
    if (listener.isErrorOccurred()) {
      throw new Aadl2RtosFailure();
    }
	}

  protected Set<Type> getUserTypes() {
    // write dispatcher types
    Set<Type> rwTypeSet = new HashSet<Type>();
    
    // "for free types" that are always necessary; void for event ports
    // and uint32_t for periodic dispatchers.  Note if the dispatcher 
    // time type changes, it may break code, so perhaps we should 
    // store the time type somewhere (model?); 
    // MWW: updated: store this in the periodic dispatcher class.
    
    rwTypeSet.add(new UnitType());
    //rwTypeSet.add(new IntType(32, false));  
    rwTypeSet.add(InputPeriodicPort.getPortType());  
    
    for (ThreadImplementation ti : model.getAllThreadImplementations()) {
      for (OutputDataPort d : ti.getOutputDataPortList()) {
        rwTypeSet.add(d.getType());
      }
      for (OutputEventPort d : ti.getOutputEventDataPortList()) {
        rwTypeSet.add(d.getType());
      }
      for (InputDataPort d : ti.getInputDataPortList()) {
        rwTypeSet.add(d.getType());
      }
      for (InputEventPort d : ti.getInputEventDataPortList()) {
        rwTypeSet.add(d.getType());
      }
    }
    for (SharedData d : model.getSharedDataList()) {
       rwTypeSet.add(d.getType());
    }
    return rwTypeSet ; 
  }

}

