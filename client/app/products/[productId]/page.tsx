export default async function Product({
    params,
}: {
    params: Promise<{ productId: string }>;
}) {
    const { productId } = await params;

    return <div>{productId}</div>;
}
