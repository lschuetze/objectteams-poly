<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2004/07/xpath-functions" xmlns:xdt="http://www.w3.org/2004/07/xpath-datatypes" exclude-result-prefixes="xs fn xdt">
   <xsl:template match="@range[../@name='org.eclipse.jdt.feature.group' and starts-with(.,'[3.6.0.v20100308-1800')]">
     <xsl:attribute name="range">[3.6.0.v20100308-1800,3.6.0.v20100308-1801)</xsl:attribute>
   </xsl:template>
   <!-- Whenever you match any node or any attribute -->
   <xsl:template match="node()|@*">
     <!-- Copy the current node -->
     <xsl:copy>
       <!-- Including any attributes it has and any child nodes -->
       <xsl:apply-templates select="@*|node()"/>
     </xsl:copy>
   </xsl:template>
</xsl:stylesheet>
