# How to create a NFS share:

## Introduction
In order for the weblogic operator to work you must have a persistent volume available to your containers that supports the Access mode of ReadWriteMany.  I would caution against trying to use VsphereVolume(s) as of 2018/8/2 it does not appear to work with the access mode. For most enterprises provisioning an NFS share for your use of the size necessary (10GB) should be an easy task, however for completeness and to make it easy for anyone to build out a full weblogic domain in an internal Pivotal PEZ Lab these instructions are provided.

## Getting the FreeNas ISO and uploading it into Vsphere.
  To serve our NFS share we're going to utilize FreeNas - a FreeBSD based distribution designed to serve up various types of network shares. We'll be installing this distribution into Vsphere.  The first step in this process is to download the FreeNAS ISO and upload it into Vsphere.

### Downloading the FreeNAS.

Visit http://www.freenas.org/download-freenas-release/ and select the 'STORAGE, PLUGINS, AND VIRTUALIZATION' configuration. Other configurations may work, but this is the one we've tested.

### Uploading the ISO to vSphere
Once the download is complete you need to log into log into vCenter using the username and secret provided as part of your HAAS installation. To put it on vsphere click the data storage Icon ![VSphere Storage](Step1_VSphere_Storage.png) and select one of the available volumes within your Vpshere cluster.

Click the 'Upload Files' button. Select the ISO you downloaded and start the upload process.

## Using the uploaded ISO to create a new virtual machine installation of FreeNas.

### Prepare the virtual machine.
Open back up the virtual machine pane, select your cluster, then use the actions menu to click "Create New Virutal Machine" 
Step_4_Create_Virtual_Machine


- Right click, new virtual machine.
- Name the vm FreeNas
- Serlect FreeNas

- Add a second hard drive for storage.
- Two networks, one on the managment network (PKS-services) and another where the pks pods will be able to reach it (VM network).


- Certificate will fail on the first try.

adding the ISO (Connect and power on)
Open the vmware console for FreeNas- you will have lots of warning.

When you click into the console, to get out you need to press (Ctrl + Alt / Ctr + Option for Mac.)

Find the network that the API server is running on.

How to configure the network manaullay.
