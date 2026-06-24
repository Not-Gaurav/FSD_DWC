# FSD_DWC

# 🚀 Full Stack Developer - DSWC Assessment Solutions

## 📋 Overview

This repository contains comprehensive solutions for the Data Structures and Web Components (DSWC) assessment, demonstrating proficiency in Java programming, memory management, concurrency, and enterprise-level software development practices.

## 🎯 Project Structure

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Core Development |
| JVM | Latest | Runtime Environment |
| Git | Latest | Version Control |
| IntelliJ IDEA / Eclipse | Latest | IDE |

## 📚 Problem Solutions

### 1. Interstellar Mining Fleet Manager
**Memory-Efficient Data Types & Inheritance**

- Implements memory-efficient primitive data types (short, byte, float)
- Abstract base class with inheritance structure
- 2D array implementation for cargo management
- **Key Features:**
  - `calculateTotalOreWeight()` - Iterates through 2D array
  - `findHeaviestContainer()` - Finds maximum weight

### 2. Colony Grid Power Manager
**Bitwise Operations & Memory Optimization**

- Single `byte` variable for 8 sectors
- Bitwise operations for state management
- **Key Features:**
  - `turnOnSector()` - Uses Bitwise OR and Left Shift
  - `turnOffSector()` - Uses Bitwise AND, NOT, and Left Shift
  - `isSectorOn()` - Checks specific bit status

### 3. DNA Sequencer Memory Leak
**String Immutability & StringBuilder Optimization**

- Prevents OutOfMemoryError
- Optimized StringBuilder with initial capacity
- **Key Features:**
  - `ingestSequence()` - Efficient character array appending
  - `mutateDNA()` - In-place string replacement

### 4. Deep-Sea Telemetry Error Hierarchy
**Checked vs Unchecked Exceptions**

- Custom exception hierarchy
- Try-With-Resources implementation
- **Key Features:**
  - `HardwareLockException` - Checked exception
  - `SensorCorruptionException` - Unchecked exception
  - AutoCloseable implementation

### 5. Drone Hive Synchronization
**Volatile & Atomic Variables**

- Thread-safe concurrent operations
- Race condition prevention
- **Key Features:**
  - `AtomicInteger` for counter
  - `volatile` for emergency abort flag
  - No synchronized keyword usage

### 6. AWS S3 Video Chunking Buffer
**Jagged Arrays Implementation**

- Dynamic 2D array generation
- Memory-efficient storage
- **Key Features:**
  - Fibonacci-based row sizing
  - Sequential integer population
  - Sum calculation with bounds checking

### 7. High-Frequency Trading Truncation
**Primitive Casting & Data Loss**

- Demonstrates precision loss in casting
- Legacy system compatibility
- **Key Features:**
  - double → int → byte conversion
  - Mathematical defect analysis

### 8. Pass-by-Value Reference Trap
**Stack vs Heap Memory Management**

- Security data wiping demonstration
- Reference vs value semantics
- **Key Features:**
  - Incorrect reference reassignment
  - Correct index-based mutation

### 9. In-Place Image Rotation
**Algorithmic Array Manipulation**

- 90° clockwise rotation without extra arrays
- In-place memory optimization
- **Key Features:**
  - Matrix transposition
  - Row reversal technique

### 10. Garbage Collector Memory Game
**Reference Management & GC Optimization**

- Memory leak prevention
- Reference severing demonstration
- **Key Features:**
  - Intersecting reference pointers
  - Systematic reference removal
  - GC eligibility prediction

## 🚀 Getting Started

### Prerequisites
bash
Java 11 or higher
Git
Any Java IDE (IntelliJ, Eclipse, VS Code)
