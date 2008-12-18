#
# Gererated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=g77.exe

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Debug/Cygwin-Windows

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/KernelString.o \
	${OBJECTDIR}/Main.o \
	${OBJECTDIR}/Archivo.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS} dist/Debug/Cygwin-Windows/bioinformatica-kernel.exe

dist/Debug/Cygwin-Windows/bioinformatica-kernel.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/Cygwin-Windows
	${LINK.cc} -o dist/Debug/Cygwin-Windows/bioinformatica-kernel ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/KernelString.o: KernelString.cpp 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -o ${OBJECTDIR}/KernelString.o KernelString.cpp

${OBJECTDIR}/Main.o: Main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -o ${OBJECTDIR}/Main.o Main.cpp

${OBJECTDIR}/Archivo.o: Archivo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.cc) -g -o ${OBJECTDIR}/Archivo.o Archivo.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Debug
	${RM} dist/Debug/Cygwin-Windows/bioinformatica-kernel.exe

# Subprojects
.clean-subprojects:
