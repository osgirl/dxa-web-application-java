package com.sdl.dxa.tridion.mapping.impl;

import com.google.common.primitives.Ints;
import com.sdl.web.pca.client.ApiClient;
import com.sdl.web.pca.client.contentmodel.enums.ContentNamespace;
import com.sdl.web.pca.client.contentmodel.generated.BinaryComponent;
import com.sdl.web.pca.client.contentmodel.generated.BinaryVariant;
import com.sdl.web.pca.client.contentmodel.generated.BinaryVariantConnection;
import com.sdl.web.pca.client.contentmodel.generated.BinaryVariantEdge;
import com.sdl.webapp.common.api.content.ContentProvider;
import com.sdl.webapp.common.api.content.ContentProviderException;
import com.sdl.webapp.common.api.content.StaticContentItem;
import com.sdl.webapp.common.exceptions.DxaItemNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.regex.Matcher;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBinaryProviderTest {
    private static final String PATH_TO_FILES = "D:=projects=dxa=web-application-java=dxa-webapp=target=dxa-webapp"
            .replaceAll("=", Matcher.quoteReplacement(File.separator));

    private static final String LOCALIZATION_PATH = "/";
    private static final String PUB_ID = "5";
    private static final int BINARY_ID = 286;
    private final String[] files = {"ballon-burner_tcm5-297_w1024_h311_n.jpg",
            "company-news-placeholder_tcm5-286_w1024_n.png",
            "duplicate_tcm5-286_w1024_n.png",
            "invalid_name.png",
            "wall_tcm5-308.jpg"};

    @Mock
    private ContentProvider contentProvider;
    @Mock
    private ApiClient pcaClient;
    @Mock
    private WebApplicationContext webApplicationContext;

    private GraphQLBinaryContentProvider provider;
    @Mock
    private BinaryComponent binaryComponent;

    @Before
    public void setUp(){
        provider = spy(new GraphQLBinaryContentProvider(pcaClient, webApplicationContext));
        doReturn(PATH_TO_FILES).when(provider).getBasePath();
        when(pcaClient.getBinaryComponent(ContentNamespace.Sites, Ints.tryParse(PUB_ID), BINARY_ID, null, null)).thenReturn(binaryComponent);
    }

    @Test
    public void getPathToBinaryFiles(){
        Path result = provider.getPathToBinaryFiles(PUB_ID);

        assertEquals(PATH_TO_FILES + File.separator + "BinaryData" + File.separator + PUB_ID + File.separator + "media", result.toString());
    }

    @Test(expected = DxaItemNotFoundException.class)
    public void processBinaryFileNoFiles() throws Exception {
        when(pcaClient.getBinaryComponent(ContentNamespace.Sites, Ints.tryParse(PUB_ID), BINARY_ID, null, null)).thenReturn(null);

        assertNull(provider.processBinaryFile(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH, null));

        verify(provider).downloadBinary(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH);
    }

    @Test
    public void processBinaryFileDownloadBinaryComponentWithoutVariants() throws Exception {
        assertNull(provider.processBinaryFile(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH, null));

        verify(provider).downloadBinary(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH);
    }

    @Test
    public void downloadBinary() throws Exception {
        BinaryVariantConnection variants = mock(BinaryVariantConnection.class);
        when(binaryComponent.getVariants()).thenReturn(variants);
        BinaryVariantEdge edge = mock(BinaryVariantEdge.class);
        when(variants.getEdges()).thenReturn(Collections.singletonList(edge));
        BinaryVariant variant = mock(BinaryVariant.class);
        when(edge.getNode()).thenReturn(variant);
        when(variant.getDownloadUrl()).thenReturn("ballon-burner_tcm5-297_w1024_h311_n.jpg");
        when(variant.getPath()).thenReturn("/binary/39137/6723");
        StaticContentItem expected = mock(StaticContentItem.class);
        when(contentProvider.getStaticContent(variant.getPath(), PUB_ID, LOCALIZATION_PATH)).thenReturn(expected);

        StaticContentItem result = provider.downloadBinary(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH);

        verify(contentProvider).getStaticContent(variant.getPath(), PUB_ID, LOCALIZATION_PATH);
        assertSame(expected, result);
    }

    @Test(expected = ContentProviderException.class)
    public void downloadBinaryException() throws Exception {
        BinaryVariantConnection variants = mock(BinaryVariantConnection.class);
        when(binaryComponent.getVariants()).thenReturn(variants);
        BinaryVariantEdge edge = mock(BinaryVariantEdge.class);
        when(variants.getEdges()).thenReturn(Collections.singletonList(edge));
        BinaryVariant variant = mock(BinaryVariant.class);
        when(edge.getNode()).thenReturn(variant);
        when(variant.getDownloadUrl()).thenReturn("ballon-burner_tcm5-297_w1024_h311_n.jpg");
        when(variant.getPath()).thenReturn("/binary/39137/6723");
        when(contentProvider.getStaticContent(variant.getPath(), PUB_ID, LOCALIZATION_PATH)).thenThrow(new RuntimeException());

        provider.downloadBinary(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH);
    }

    @Test
    public void downloadBinaryNoEdges() throws Exception {
        BinaryVariantConnection variants = mock(BinaryVariantConnection.class);
        when(binaryComponent.getVariants()).thenReturn(variants);
        when(variants.getEdges()).thenReturn(null);

        assertNull(provider.downloadBinary(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH));
    }

    @Test
    public void processBinaryFile() throws Exception {
        StaticContentItem binaryContent = mock(StaticContentItem.class);
        when(contentProvider.getStaticContent(files[0], PUB_ID, LOCALIZATION_PATH)).thenReturn(binaryContent);

        StaticContentItem result = provider.processBinaryFile(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH, files);

        assertEquals(binaryContent, result);
    }

    @Test
    public void getBasePathWithLastSeparator() {
        doCallRealMethod().when(provider).getBasePath();
        String path = "abcd";
        doReturn(path + File.separator).when(provider).getAppRealPath();

        assertEquals(path, provider.getBasePath());
    }

    @Test
    public void getBasePath() {
        doCallRealMethod().when(provider).getBasePath();
        String path = "abcd";
        doReturn(path).when(provider).getAppRealPath();

        assertEquals(path, provider.getBasePath());
    }

    @Test
    public void getStaticContent() throws Exception {
        Path pathToBinaries = mock(Path.class);
        doReturn(pathToBinaries).when(provider).getPathToBinaryFiles(PUB_ID);
        doReturn(files).when(provider).getFiles(BINARY_ID, PUB_ID, pathToBinaries);
        StaticContentItem binaryContent = mock(StaticContentItem.class);
        doReturn(binaryContent).when(provider).processBinaryFile(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH, files);

        StaticContentItem result = provider.getStaticContent(contentProvider, BINARY_ID, PUB_ID, LOCALIZATION_PATH);

        assertEquals(binaryContent, result);
    }

    @Test
    public void getFilenameFilter() {
        File file = mock(File.class);

        assertFalse(provider.getFilenameFilter(BINARY_ID, PUB_ID).accept(file, files[0]));
        assertTrue(provider.getFilenameFilter(BINARY_ID, PUB_ID).accept(file, files[1]));
        assertTrue(provider.getFilenameFilter(BINARY_ID, PUB_ID).accept(file, files[2]));
        assertFalse(provider.getFilenameFilter(BINARY_ID, PUB_ID).accept(file, files[3]));
        assertFalse(provider.getFilenameFilter(BINARY_ID, PUB_ID).accept(file, files[4]));
    }
}