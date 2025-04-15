import os
import pandas as pd
import matplotlib.pyplot as plt

# Set the directory where your .data files are located
directory = "./TcpHtcp"


def plot_metrics_per_flow(directory):
    metrics = ["cwnd", "inflight", "next-rx", "next-tx", "rto", "rtt", "ssth"]
    flows = set()  # To track unique flows based on filenames

    # Identify flows based on filenames
    for filename in os.listdir(directory):
        if filename.endswith(".data"):
            parts = filename.split("-")
            for part in parts:
                if part.startswith("flow") and part[4:].isdigit():
                    flows.add(part)

    # Sort flows for consistent processing
    flows = sorted(flows)

    # Loop through each flow
    for flow in flows:
        # Loop through each metric
        for metric in metrics:
            combined_data = []

            # Find all files for the current flow and metric
            for filename in os.listdir(directory):
                if flow in filename and metric in filename and filename.endswith(".data"):
                    file_path = os.path.join(directory, filename)
                    print(f"Processing file: {filename}")

                    try:
                        # Read the file into a DataFrame
                        data = pd.read_csv(
                            file_path, delim_whitespace=True, header=None, names=["Time", "Value"])
                        # Drop duplicate Time values
                        data = data.drop_duplicates(subset=["Time"])
                        # Ensure the data is sorted by Time
                        data = data.sort_values(by="Time")
                        combined_data.append(data)
                    except Exception as e:
                        print(f"Error reading {filename}: {e}")

            # Combine all data for the current flow and metric
            if combined_data:
                combined_df = pd.concat(combined_data)

                # Group by Time and calculate the average Value
                avg_data = combined_df.groupby("Time", as_index=False).mean()

                # Plot the average data
                plt.figure(figsize=(10, 6))
                plt.plot(avg_data["Time"], avg_data["Value"],
                         label=f"{flow}-{metric}", linewidth=1.5)
                plt.title(f"{metric} for {flow} over Time")
                plt.xlabel("Time (s)")
                plt.ylabel(metric.capitalize())
                plt.legend()
                plt.grid(True, linestyle="--", alpha=0.7)

                # Save the plot as a PNG file
                output_filename = f"./TcpHtcp/{flow}_{metric}.png"
                # Save with high resolution
                plt.savefig(output_filename, dpi=300)
                plt.close()
                print(f"Saved plot for {flow}-{metric} as {output_filename}")


# Call the function to generate and save plots
plot_metrics_per_flow(directory)
