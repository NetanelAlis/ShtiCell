# ShtiCell

**ShtiCell** is a Java-based spreadsheet application inspired by popular tools like Google Sheets and Excel. It combines familiar spreadsheet functionalities with a unique, flexible, and advanced implementation.

## Overview

ShtiCell is a three-stage project, evolving through multiple levels of functionality:

1. **Console Application**: Initial version with basic spreadsheet operations accessed through a command-line interface.
2. **JavaFX GUI Application**: Adds a graphical user interface for improved usability and additional features like data manipulation and cell formatting.
3. **Client-Server Application**: Enables multi-user access, remote editing, and real-time collaboration on shared sheets via a client-server model.

## Features

- **Core Spreadsheet Operations**: Supports typical spreadsheet features, with cells that can store numeric, string, and boolean values.
- **Cell Dependencies**: Allows cells to reference each other, updating dynamically across the sheet when changes occur.
- **Version Control**: Tracks and manages changes with version history, enabling users to view or revert to previous states.
- **Enhanced User Interface**: The JavaFX GUI provides tools for customizing cell appearance, sorting, filtering, and more.
- **Multi-User Collaboration**: The client-server model facilitates real-time collaboration, with permissions and access control for secure sharing.
- **Task Threads for Efficiency**: Heavy task like file processing run in separate thread, ensuring a smooth user experience.
- **and much more!**

## Getting Started

1. **Clone the Repository**: Download the ShtiCell repository to your local machine.
2. **Run the Application**:
   - For the console version, compile and run the basic command-line application.
   - For the GUI version, compile and run the JavaFX-based application for a more interactive experience.
   - For the client-server setup, deploy the server module using Apache Tomcat 10 and connect clients for multi-user functionality.

       

# ShtiCell Permissions Management Screen

This ShtiCell screen serves as a central dashboard for managing sheet permissions and collaboration. Users can view available sheets, submit permission requests, and handle incoming requests, all from one interface.

- **Available Sheets**: Lists sheets accessible to the user, showing the user’s name, sheet name, size, and current permission level (e.g., "Owner" or "None"). A "View Sheet" button allows users to open selected sheets.

- **Submit New Permission Request**: Users can request access to specific sheets by entering the sheet name and selecting a permission type (e.g., "Owner" or "Writer"). The request is sent by clicking "Send."

- **Received Permission Requests**: Displays incoming access requests from other users, including the sender’s name, requested sheet, and permission type. Users can approve or reject requests using the buttons provided.

- **Sheet Permission History**: Shows a log of permission changes, listing user names, permission types, and request statuses, such as "Approved" or "Pending."

This layout facilitates smooth collaboration by organizing all permission-related functions, making it easy to control and share access to sheets within ShtiCell.

  ![Screenshot 2024-11-10 140157](https://github.com/user-attachments/assets/c2b342a0-8069-4774-bf62-4efa095e58fa)

## Sheet Overview and Version Control

This screen in *ShtiCell* provides a central view of a selected sheet, along with tools for version management and cell customization.

- *Sheet Overview*: Displays the spreadsheet with data organized in rows and columns, allowing users to view and interact with their data directly.

- *Cell Details*: Shows information about the selected cell, including its **Cell ID** and original value. Users can modify the cell value by clicking *Update Value*.

- *Version Control*: Allows users to navigate between different versions of the sheet. This feature helps in tracking changes and reverting to previous versions if necessary.

- *Sidebar Menu*: Provides quick access to **Commands, **Ranges, and **Customize* options, enabling efficient data manipulation and customization.

This interface supports seamless data management, version tracking, and customization for a flexible and user-friendly experience within ShtiCell.

![image](https://github.com/user-attachments/assets/d36aa1bf-b3d4-4ebc-9942-ec91cda035f5)

## Commands

The **Commands** section in ShtiCell provides an array of tools for data manipulation, analysis, and visualization, allowing users to perform complex operations on selected cell ranges. This section includes:

- **Select Range**: Allows users to define a cell range by specifying the **Top Right Boundary** and **Bottom Left Boundary** (e.g., "A2" to "D4"). This feature enables users to focus operations on a specific area within the spreadsheet.

- **Sort**: Enables sorting of data within the selected range. Users can specify columns to sort by entering column identifiers separated by a semicolon. The **Sort** button applies the chosen order to the data.

- **Filter**: Provides filtering options to refine the view. Users can select a specific column and items to filter, displaying only relevant data within a large dataset.

- **Graph**: Allows users to create visual representations of their data. Users can choose the type of graph from a dropdown menu and generate a graph based on the selected data.

- **Dynamic Analysis**: This tool enables users to conduct variable analysis on a specific cell. Key components include:
  - **Cell ID**: Specifies the cell (e.g., "D3") to analyze.
  - **Min Value, Step Size, Max Value**: Fields for defining the range and intervals of values for analysis.
  - **Slider**: Allows for dynamic adjustment of values across the specified range.
  - **Add (+) and Finish Analysis**: The **+** button adds new parameters for analysis, and the **Finish Analysis** button completes the setup.

These commands collectively enhance ShtiCell's capabilities, providing users with intuitive controls for managing, analyzing, and visualizing spreadsheet data, all within a streamlined interface.

![Screenshot 2024-11-10 142022](https://github.com/user-attachments/assets/51bb2632-f128-4cda-a38e-4f0efd591bf0)
![Screenshot 2024-11-10 145712](https://github.com/user-attachments/assets/f08e1287-8def-48a2-b910-045819fe4be8)

## Ranges

The **Ranges** feature in ShtiCell allows users to define specific areas within the spreadsheet, making it easier to apply functions and manage data efficiently.

- **Add New Range**: Users can create a new range by specifying a name, **Top Right Boundary**, and **Bottom Left Boundary** (e.g., "A2" to "D4"). Once defined, the range is saved, and the area within these boundaries can be referenced and manipulated as a single unit.

- **Active Ranges**: The list of all currently defined ranges is displayed under **Active Ranges**. Each range has a label that includes its name and cell coordinates (e.g., "grades C3..C5").

- **Functions**: When creating or selecting a range, users can apply various functions to the data within it. Common functions include:
  - **Sum (Plus)**: Adds up all values within the range, providing a total.
  - **Average (Avg)**: Calculates the average of the values within the range, offering quick insights into the data.

These functionalities simplify calculations across multiple cells, making data management and analysis more efficient within ShtiCell. By defining ranges, users can quickly perform calculations like totals and averages without manually entering each cell, streamlining workflows for larger datasets.

![image](https://github.com/user-attachments/assets/61d1758e-9c3d-4598-b75f-6f8a4bec39a3)

## Customize

The **Customize** section in ShtiCell allows users to personalize the appearance and layout of their spreadsheet cells, offering control over cell size, alignment, and color customization.

- **Column/Row Size**: Users can adjust the width of specific columns and the height of rows by entering values in the respective fields. This feature helps to optimize the layout for different data presentations.

- **Text Alignment**: Provides options to align text within cells to the left, center, or right. This allows for better visual organization of data.

- **Cell Customizations**: Users can personalize individual cells by selecting a **Background Color** and **Text Color**. This feature enables color-coding for different types of data, making it easier to identify and interpret information at a glance. The **Selected Cell** label displays the currently active cell (e.g., "C3"), so users know which cell is being customized.

- **Reset**: The **Reset** button allows users to revert any customizations back to the default settings, making it easy to clear formatting changes.

The **Customize** section enhances the usability of ShtiCell by providing options to tailor the spreadsheet's look and feel, allowing users to create visually appealing and organized data presentations.

![Screenshot 2024-11-10 143227](https://github.com/user-attachments/assets/89da4cbe-66c4-4f6f-a49d-8defe10f87ad)

